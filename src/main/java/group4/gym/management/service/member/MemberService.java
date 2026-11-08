package group4.gym.management.service.member;

import group4.gym.management.dao.member.MemberDAO;
import group4.gym.management.dao.registration.RegistrationDAO;
import group4.gym.management.database.DBContext;
import group4.gym.management.exception.AccessDeniedException;
import group4.gym.management.exception.AppException;
import group4.gym.management.exception.BusinessRuleException;
import group4.gym.management.exception.NotFoundException;
import group4.gym.management.exception.ValidationException;
import group4.gym.management.model.account.Role;
import group4.gym.management.model.account.SessionUser;
import group4.gym.management.model.member.ExpiringMember;
import group4.gym.management.model.member.Member;
import group4.gym.management.model.member.MemberDetailView;
import group4.gym.management.model.member.MemberListItem;
import group4.gym.management.model.member.MemberScope;
import group4.gym.management.model.member.MembershipStatus;
import group4.gym.management.model.registration.Registration;
import group4.gym.management.model.registration.RegistrationStatus;
import group4.gym.management.util.AppClock;
import group4.gym.management.util.AppConstants;
import group4.gym.management.util.Page;
import group4.gym.management.util.ValidationUtil;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Member business logic.
 * Invalid data throws ValidationException, rule violations throw BusinessRuleException,
 * MANAGER-only data requested by a RECEPTIONIST throws AccessDeniedException, unknown IDs throw NotFoundException.
 */
public class MemberService {

    private final MemberDAO memberDAO = new MemberDAO();
    private final RegistrationDAO registrationDAO = new RegistrationDAO();
    private final MembershipStatusService membershipStatusService = new MembershipStatusService();

    /**
     * Searches member names or phone numbers and returns a page with membership statuses.
     * Only managers may include deleted profiles.
     */
    public Page<MemberListItem> search(SessionUser actor, String keyword, MemberScope scope, int page) {
        requireStaff(actor);
        if (scope == null) {
            scope = MemberScope.ACTIVE;
        }
        if (scope != MemberScope.ACTIVE) {
            requireManager(actor);
        }
        if (keyword != null) {
            keyword = keyword.trim();
        }
        if (!ValidationUtil.isValidKeyword(keyword)) {
            throw new ValidationException("Invalid keyword.",
                    Map.of("keyword", "Use at most 100 characters."));
        }

        if (page < 1) {
            page = 1;
        }
        LocalDate today = AppClock.today();
        int totalMembers = memberDAO.count(keyword, scope);
        int offset = Page.offsetOf(page, AppConstants.PAGE_SIZE);
        List<MemberListItem> items = memberDAO.search(keyword, scope, today,
                offset, AppConstants.PAGE_SIZE);
        for (MemberListItem item : items) {
            MembershipStatus status = membershipStatusService.resolve(
                    item.isPaidCoveringToday(), item.getCurrentEndDate(), today);
            item.setStatus(status);
        }
        return new Page<>(items, page, AppConstants.PAGE_SIZE, totalMembers);
    }

    /**
     * Loads a member profile, membership status, expiry warning and registration history.
     * Deleted profiles are visible only to managers.
     */
    public MemberDetailView getDetail(SessionUser actor, int memberId) {
        requireStaff(actor);
        Member member = requireMember(memberId);
        if (member.isDeleted()) {
            requireManager(actor);
        }

        LocalDate today = AppClock.today();
        LocalDate currentEndDate = null;
        boolean coversToday = false;
        List<Registration> registrations = registrationDAO.findByMember(memberId);
        for (Registration registration : registrations) {
            if (registration.getStatus() == RegistrationStatus.PAID) {
                LocalDate startDate = registration.getStartDate();
                LocalDate endDate = registration.getEndDate();

                if (currentEndDate == null) {
                    currentEndDate = endDate;
                } else if (endDate.isAfter(currentEndDate)) {
                    currentEndDate = endDate;
                }

                boolean hasStarted = !startDate.isAfter(today);
                boolean hasNotEnded = !endDate.isBefore(today);
                if (hasStarted && hasNotEnded) {
                    coversToday = true;
                }
            }
        }

        MembershipStatus status = membershipStatusService.resolve(coversToday, currentEndDate, today);
        boolean expiringSoon = false;
        if (!member.isDeleted()) {
            expiringSoon = membershipStatusService.isExpiringSoon(status, currentEndDate, today);
        }

        MemberDetailView detail = new MemberDetailView();
        detail.setMember(member);
        detail.setRegistrations(registrations);
        detail.setCurrentEndDate(currentEndDate);
        detail.setStatus(status);
        detail.setExpiringSoon(expiringSoon);
        return detail;
    }

    /**
     * Loads a profile for an authorized edit request and rejects deleted profiles.
     */
    public Member getForEdit(int memberId) {
        Member member = requireMember(memberId);
        if (member.isDeleted()) {
            throw new BusinessRuleException("Restore this member profile before editing it.");
        }
        return member;
    }

    /**
     * Validates and creates a profile with the current creation time and staff account.
     */
    public int createMember(SessionUser actor, Member member) {
        requireStaff(actor);
        validateMember(member, null);
        member.setDeleted(false);
        member.setCreatedAt(AppClock.now());
        member.setCreatedBy(actor.getId());
        return memberDAO.insert(member);
    }

    /**
     * Validates and updates an existing profile while preserving its creation information.
     */
    public void updateMember(SessionUser actor, Member member) {
        requireStaff(actor);
        if (member == null || member.getId() == null) {
            throw new NotFoundException("Member not found.");
        }
        Member existingMember = getForEdit(member.getId());
        validateMember(member, existingMember.getId());
        member.setCreatedAt(existingMember.getCreatedAt());
        member.setCreatedBy(existingMember.getCreatedBy());
        member.setDeleted(false);

        boolean updated = memberDAO.update(member);
        if (!updated) {
            throw new BusinessRuleException("The member is missing or was deleted. Please reload the profile.");
        }
    }

    /**
     * Deletes a profile and cancels its pending registrations in one transaction.
     * Only managers may delete, and any unexpired paid package blocks deletion.
     */
    public void softDelete(SessionUser actor, int memberId) {
        requireManager(actor);
        if (memberId <= 0) {
            throw new NotFoundException("Member not found.");
        }
        try (Connection conn = DBContext.getConnection()) {
            conn.setAutoCommit(false);
            try {
                Member member = memberDAO.lockForUpdate(conn, memberId);
                if (member == null) {
                    throw new NotFoundException("Member not found.");
                }
                if (member.isDeleted()) {
                    throw new BusinessRuleException("The member profile is already deleted.");
                }
                boolean hasUnexpiredPackage = registrationDAO.hasUnexpiredPaid(
                        conn, memberId, AppClock.today());
                if (hasUnexpiredPackage) {
                    throw new BusinessRuleException("Cannot delete a member with an unexpired paid package.");
                }
                boolean deleted = memberDAO.markDeleted(conn, memberId);
                if (!deleted) {
                    throw new BusinessRuleException("The member profile could not be deleted.");
                }
                registrationDAO.cancelAllPending(conn, memberId, actor.getId(),
                        AppConstants.CANCEL_REASON_MEMBER_DELETED);
                conn.commit();
            } catch (SQLException | RuntimeException ex) {
                DBContext.rollbackQuietly(conn);
                throw ex;
            }
        } catch (SQLException ex) {
            throw new AppException("Cannot delete member " + memberId, ex);
        }
    }

    /**
     * Restores a deleted profile for a manager without changing its registration history.
     */
    public void restore(SessionUser actor, int memberId) {
        requireManager(actor);
        Member member = requireMember(memberId);
        if (!member.isDeleted()) {
            throw new BusinessRuleException("The member profile is not deleted.");
        }
        boolean restored = memberDAO.restore(memberId);
        if (!restored) {
            throw new BusinessRuleException("The member profile changed. Please reload it.");
        }
    }

    /**
     * Returns the expiring-soon list for an authorized staff request.
     */
    public List<ExpiringMember> listExpiringSoon() {
        return membershipStatusService.expiringSoon();
    }

    /**
     * Loads a member or reports an invalid or missing profile ID.
     */
    private Member requireMember(int memberId) {
        if (memberId <= 0) {
            throw new NotFoundException("Member not found.");
        }
        Member member = memberDAO.findById(memberId);
        if (member == null) {
            throw new NotFoundException("Member not found.");
        }
        return member;
    }

    /**
     * Normalizes profile fields and rejects invalid data or a phone owned by another profile.
     */
    private void validateMember(Member member, Integer excludeId) {
        if (member == null) {
            throw new ValidationException("Member data is required.",
                    Map.of("member", "Member data is required."));
        }
        if (member.getFullName() != null) {
            member.setFullName(member.getFullName().trim());
        }
        if (member.getPhone() != null) {
            member.setPhone(member.getPhone().trim());
        }
        if (member.getEmail() != null) {
            member.setEmail(member.getEmail().trim());
            if (member.getEmail().isEmpty()) {
                member.setEmail(null);
            }
        }

        Map<String, String> errors = new LinkedHashMap<>();
        if (!isValidMemberName(member.getFullName())) {
            errors.put("fullName", "Use 2-100 characters without digits.");
        }
        if (!ValidationUtil.isValidPhone(member.getPhone())) {
            errors.put("phone", "Use 10 digits starting with 0.");
        }
        if (member.getEmail() != null && !ValidationUtil.isValidEmail(member.getEmail())) {
            errors.put("email", "Enter a valid email address with at most 254 characters.");
        }
        if (member.getDateOfBirth() != null
                && !ValidationUtil.isValidDateOfBirth(member.getDateOfBirth())) {
            errors.put("dateOfBirth", "Use a date between 1900-01-01 and today.");
        }
        if (!errors.isEmpty()) {
            throw new ValidationException("Please correct the member details.", errors);
        }

        Member phoneOwner = memberDAO.findByPhone(member.getPhone());
        if (phoneOwner != null) {
            boolean belongsToSameMember = phoneOwner.getId().equals(excludeId);
            if (!belongsToSameMember) {
                String message = "This phone number is already associated with member profile "
                        + phoneOwner.getCode() + ".";
                errors.put("phone", message);
                throw new ValidationException(message, errors);
            }
        }
    }

    /**
     * Rejects blank names and digits, including Unicode spaces and digits.
     */
    private boolean isValidMemberName(String name) {
        if (!ValidationUtil.isValidFullName(name)) {
            return false;
        }
        boolean hasContent = false;
        for (int character : name.codePoints().toArray()) {
            if (Character.isDigit(character)) {
                return false;
            }
            if (!Character.isWhitespace(character) && !Character.isSpaceChar(character)) {
                hasContent = true;
            }
        }
        return hasContent;
    }

    /**
     * Restricts member operations to managers and receptionists.
     */
    private void requireStaff(SessionUser actor) {
        if (actor == null) {
            throw new AccessDeniedException("Manager or receptionist access is required.");
        }
        boolean isManager = actor.getRole() == Role.MANAGER;
        boolean isReceptionist = actor.getRole() == Role.RECEPTIONIST;
        if (!isManager && !isReceptionist) {
            throw new AccessDeniedException("Manager or receptionist access is required.");
        }
    }

    /**
     * Restricts deletion, restoration and deleted-profile access to managers.
     */
    private void requireManager(SessionUser actor) {
        requireStaff(actor);
        if (actor.getRole() != Role.MANAGER) {
            throw new AccessDeniedException("Manager access is required.");
        }
    }
}
