package group4.gym.management.service.registration;

import group4.gym.management.dao.member.MemberDAO;
import group4.gym.management.dao.registration.PackageDAO;
import group4.gym.management.dao.registration.RegistrationDAO;
import group4.gym.management.database.DBContext;
import group4.gym.management.exception.AppException;
import group4.gym.management.exception.BusinessRuleException;
import group4.gym.management.exception.NotFoundException;
import group4.gym.management.exception.ValidationException;
import group4.gym.management.model.account.SessionUser;
import group4.gym.management.model.member.Member;
import group4.gym.management.model.registration.DateAxis;
import group4.gym.management.model.registration.GymPackage;
import group4.gym.management.model.registration.Registration;
import group4.gym.management.model.registration.RegistrationFilter;
import group4.gym.management.model.registration.RegistrationView;
import group4.gym.management.util.AppClock;
import group4.gym.management.util.AppConstants;
import group4.gym.management.util.DateRange;
import group4.gym.management.util.Page;
import group4.gym.management.util.ValidationUtil;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * Registrations (CN-22 to CN-24, QT-02 to QT-04, QT-06, QT-11, QT-13).
 */
public class RegistrationService {

    /** Max members offered in the CN-22 member picker. */
    private static final int SELECTABLE_MEMBER_LIMIT = 50;
    private static final String MSG_KEYWORD = "Từ khóa tối đa 100 ký tự";

    private final RegistrationDAO registrationDAO = new RegistrationDAO();
    private final PackageDAO packageDAO = new PackageDAO();
    private final MemberDAO memberDAO = new MemberDAO();

    /**
     * Members that may receive a registration: not soft-deleted (CN-22, QT-11).
     */
    public List<Member> findSelectableMembers(String keyword) {
        String trimmed = keyword == null ? null : keyword.trim();
        if (!ValidationUtil.isValidKeyword(trimmed)) {
            throw new ValidationException("Invalid keyword", Map.of("keyword", MSG_KEYWORD));
        }
        return memberDAO.searchActive(trimmed == null || trimmed.isEmpty() ? null : trimmed, SELECTABLE_MEMBER_LIMIT);
    }

    /**
     * One member to pre-select in the CN-22 form (button on the member detail page).
     */
    public Member getSelectableMember(int memberId) {
        Member member = memberDAO.findById(memberId);
        if (member == null) {
            throw new NotFoundException("Member " + memberId + " not found");
        }
        if (member.isDeleted()) {
            throw new BusinessRuleException("Hội viên đã bị xóa, không thể đăng ký gói");
        }
        return member;
    }

    /**
     * Create a PENDING registration (CN-22) in ONE transaction (QT-06):
     * the member must exist and not be soft-deleted (QT-11), the package must be on sale (QT-04, also when POSTed directly),
     * and the snapshot of name/duration/price is copied from the database, never from the form (QT-03).
     *
     * @return ID of the new registration (the servlet redirects to the CN-25 review step)
     */
    public int createPending(SessionUser actor, int memberId, int packageId) {
        GymPackage gymPackage = packageDAO.findById(packageId);
        if (gymPackage == null) {
            throw new NotFoundException("Package " + packageId + " not found");
        }
        if (!gymPackage.isActive()) {
            throw new BusinessRuleException("Gói tập đã ngừng bán");
        }

        try (Connection conn = DBContext.getConnection()) {
            conn.setAutoCommit(false);
            try {
                Member member = memberDAO.lockForUpdate(conn, memberId);
                if (member == null) {
                    throw new NotFoundException("Member " + memberId + " not found");
                }
                if (member.isDeleted()) {
                    throw new BusinessRuleException("Hội viên đã bị xóa, không thể đăng ký gói");
                }

                Registration registration = new Registration();
                registration.setMemberId(memberId);
                registration.setPackageId(gymPackage.getId());
                registration.setPackageNameSnapshot(gymPackage.getName());
                registration.setDurationMonthsSnapshot(gymPackage.getDurationMonths());
                registration.setPriceSnapshot(gymPackage.getPrice());
                registration.setCreatedAt(AppClock.now());
                registration.setCreatedBy(actor.getId());
                int id = registrationDAO.insert(conn, registration);
                conn.commit();
                return id;
            } catch (SQLException | RuntimeException ex) {
                DBContext.rollbackQuietly(conn);
                throw ex;
            }
        } catch (SQLException ex) {
            throw new AppException("Cannot create registration for member " + memberId, ex);
        }
    }

    /**
     * Cancel a PENDING registration with a reason (CN-23, QT-02, HL-15). PAID and CANCELLED never change.
     */
    public void cancel(SessionUser actor, int registrationId, String reason) {
        String trimmed = reason == null ? null : reason.trim();
        if (!ValidationUtil.isValidCancelReason(trimmed)) {
            throw new ValidationException("Invalid cancel reason", Map.of("reason", "Lý do hủy phải từ 3 đến 255 ký tự"));
        }
        if (registrationDAO.findById(registrationId) == null) {
            throw new NotFoundException("Registration " + registrationId + " not found");
        }
        if (!registrationDAO.cancelIfPending(registrationId, actor.getId(), trimmed, AppClock.now())) {
            throw new BusinessRuleException(AppConstants.MSG_REGISTRATION_NOT_PENDING);
        }
    }

    /**
     * Registration/transaction list (CN-24), 20 rows per page.
     */
    public Page<RegistrationView> search(RegistrationFilter filter) {
        String keyword = filter.getMemberKeyword() == null ? null : filter.getMemberKeyword().trim();
        if (!ValidationUtil.isValidKeyword(keyword)) {
            throw new ValidationException("Invalid keyword", Map.of("memberKeyword", MSG_KEYWORD));
        }
        DateRange range = DateRange.of(filter.getFromDate(), filter.getToDate());
        if (filter.getAxis() == null) {
            filter.setAxis(DateAxis.CREATED_AT);
        }
        int page = Math.max(filter.getPage(), 1);

        int total = registrationDAO.count(filter, range.getFromInclusive(), range.getToExclusive());
        List<RegistrationView> items = registrationDAO.search(filter, range.getFromInclusive(), range.getToExclusive(),
                Page.offsetOf(page, AppConstants.PAGE_SIZE), AppConstants.PAGE_SIZE);
        return new Page<>(items, page, AppConstants.PAGE_SIZE, total);
    }
}
