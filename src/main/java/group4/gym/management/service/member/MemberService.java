package group4.gym.management.service.member;

import group4.gym.management.dao.member.MemberDAO;
import group4.gym.management.dao.registration.RegistrationDAO;
import group4.gym.management.model.account.SessionUser;
import group4.gym.management.model.member.ExpiringMember;
import group4.gym.management.model.member.Member;
import group4.gym.management.model.member.MemberDetailView;
import group4.gym.management.model.member.MemberListItem;
import group4.gym.management.model.member.MemberScope;
import group4.gym.management.util.Page;
import java.util.List;

/**
 * Member business logic (FR-11 to FR-17, BR-01, BR-06, BR-09 to BR-11).
 * Invalid data throws ValidationException, rule violations throw BusinessRuleException,
 * MANAGER-only data requested by a RECEPTIONIST throws AccessDeniedException, unknown IDs throw NotFoundException.
 */
public class MemberService {

    private final MemberDAO memberDAO = new MemberDAO();
    private final RegistrationDAO registrationDAO = new RegistrationDAO();
    private final MembershipStatusService membershipStatusService = new MembershipStatusService();

    /**
     * Member list and search (FR-11), 20 rows per page, newest first.
     * BR-11: scope DELETED/ALL is MANAGER only.
     */
    public Page<MemberListItem> search(SessionUser actor, String keyword, MemberScope scope, int page) {
        // TODO: scope null -> ACTIVE; scope != ACTIVE && actor is not MANAGER -> AccessDeniedException;
        //       VR-07 keyword length; memberDAO.search(keyword, scope, AppClock.today(), Page.offsetOf(page, PAGE_SIZE), PAGE_SIZE);
        //       for each row: status = membershipStatusService.resolve(...); memberDAO.count for the total.
        // Output: Page of MemberListItem.
        return null;
    }

    /**
     * Member detail (FR-13): profile, soft-delete label, status today, expiry warning, registrations of every status.
     * BR-11: a soft-deleted profile is visible to MANAGER only.
     */
    public MemberDetailView getDetail(SessionUser actor, int memberId) {
        // TODO: memberDAO.findById; null -> NotFoundException; deleted && actor is not MANAGER -> AccessDeniedException;
        //       registrations = registrationDAO.findByMember(memberId) (newest first);
        //       status / currentEndDate from the PAID rows (MembershipStatusService);
        //       expiringSoon = !deleted && membershipStatusService.isExpiringSoon(status, currentEndDate, AppClock.today()).
        // Output: MemberDetailView.
        return null;
    }

    /**
     * Load a profile for the edit form (FR-14). BR-11: a soft-deleted profile cannot be edited.
     */
    public Member getForEdit(int memberId) {
        // TODO: memberDAO.findById; null -> NotFoundException; deleted -> BusinessRuleException.
        // Output: Member.
        return null;
    }

    /**
     * Create a profile, also before any package is bought (FR-12, BR-01).
     * A duplicate phone (including soft-deleted profiles) is refused and the error shows the code of the profile using it.
     */
    public int createMember(SessionUser actor, Member member) {
        // TODO: Validate VR-01, VR-02, VR-04, VR-05, VR-06 -> ValidationException;
        //       memberDAO.findByPhone != null -> field error on "phone" containing existing.getCode() (BR-10: restore instead);
        //       createdAt = AppClock.now(); createdBy = actor.getId(); memberDAO.insert.
        // Output: ID of the new member.
        return 0;
    }

    /**
     * Update a profile that is not soft-deleted (FR-14). Code and created_at are not editable.
     */
    public void updateMember(SessionUser actor, Member member) {
        // TODO: getForEdit; validate as createMember; findByPhone belongs to another id -> field error with its code;
        //       memberDAO.update == false -> BusinessRuleException (deleted meanwhile).
        // Output: nothing returned; throws an exception on failure.
    }

    /**
     * Soft-delete a member (FR-15) in ONE transaction (BR-06, BR-09, NFR-06):
     * refused while any PAID has end_date >= today; otherwise is_deleted = 1 and every PENDING becomes CANCELLED
     * with the reason "Xóa hồ sơ hội viên"; PAID rows are untouched.
     */
    public void softDelete(SessionUser actor, int memberId) {
        // TODO: conn = DBContext.getConnection(); setAutoCommit(false);
        //       memberDAO.lockForUpdate(conn, memberId) null -> NotFoundException; already deleted -> BusinessRuleException;
        //       registrationDAO.hasUnexpiredPaid(conn, memberId, AppClock.today()) -> BusinessRuleException with the reason (AC-11);
        //       memberDAO.markDeleted(conn, memberId);
        //       registrationDAO.cancelAllPending(conn, memberId, actor.getId(), AppConstants.CANCEL_REASON_MEMBER_DELETED);
        //       commit; any error -> DBContext.rollbackQuietly and rethrow.
        // Output: nothing returned; throws an exception when refused.
    }

    /**
     * Restore a soft-deleted profile (FR-16, BR-10). Code, created_at and history are kept; CANCELLED rows stay CANCELLED.
     */
    public void restore(SessionUser actor, int memberId) {
        // TODO: memberDAO.findById null -> NotFoundException; not deleted -> BusinessRuleException; memberDAO.restore.
        // Output: nothing returned; throws an exception on failure.
    }

    /**
     * Expiring-soon list (FR-17, BR-08), display only.
     */
    public List<ExpiringMember> listExpiringSoon() {
        // TODO: membershipStatusService.expiringSoon().
        // Output: list of ExpiringMember sorted by end date ascending.
        return null;
    }
}
