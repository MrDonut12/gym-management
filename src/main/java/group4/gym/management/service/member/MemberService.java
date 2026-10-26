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
 * Member business logic (CN-11 to CN-17, QT-01, QT-06, QT-09 to QT-11).
 * Invalid data throws ValidationException, rule violations throw BusinessRuleException,
 * MANAGER-only data requested by a RECEPTIONIST throws AccessDeniedException, unknown IDs throw NotFoundException.
 */
public class MemberService {

    private final MemberDAO memberDAO = new MemberDAO();
    private final RegistrationDAO registrationDAO = new RegistrationDAO();
    private final MembershipStatusService membershipStatusService = new MembershipStatusService();

    /**
     * Member list and search (CN-11), 20 rows per page, newest first.
     * QT-11: scope DELETED/ALL is MANAGER only.
     */
    public Page<MemberListItem> search(SessionUser actor, String keyword, MemberScope scope, int page) {
        // TODO: scope null -> ACTIVE; scope != ACTIVE && actor is not MANAGER -> AccessDeniedException;
        //       HL-07 keyword length; memberDAO.search(keyword, scope, AppClock.today(), Page.offsetOf(page, PAGE_SIZE), PAGE_SIZE);
        //       for each row: status = membershipStatusService.resolve(...); memberDAO.count for the total.
        // Output: Page of MemberListItem.
        return null;
    }

    /**
     * Member detail (CN-13): profile, soft-delete label, status today, expiry warning, registrations of every status.
     * QT-11: a soft-deleted profile is visible to MANAGER only.
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
     * Load a profile for the edit form (CN-14). QT-11: a soft-deleted profile cannot be edited.
     */
    public Member getForEdit(int memberId) {
        // TODO: memberDAO.findById; null -> NotFoundException; deleted -> BusinessRuleException.
        // Output: Member.
        return null;
    }

    /**
     * Create a profile, also before any package is bought (CN-12, QT-01).
     * A duplicate phone (including soft-deleted profiles) is refused and the error shows the code of the profile using it.
     */
    public int createMember(SessionUser actor, Member member) {
        // TODO: Validate HL-01, HL-02, HL-04, HL-05, HL-06 -> ValidationException;
        //       memberDAO.findByPhone != null -> field error on "phone" containing existing.getCode() (QT-10: restore instead);
        //       createdAt = AppClock.now(); createdBy = actor.getId(); memberDAO.insert.
        // Output: ID of the new member.
        return 0;
    }

    /**
     * Update a profile that is not soft-deleted (CN-14). Code and created_at are not editable.
     */
    public void updateMember(SessionUser actor, Member member) {
        // TODO: getForEdit; validate as createMember; findByPhone belongs to another id -> field error with its code;
        //       memberDAO.update == false -> BusinessRuleException (deleted meanwhile).
        // Output: nothing returned; throws an exception on failure.
    }

    /**
     * Soft-delete a member (CN-15) in ONE transaction (QT-06, QT-09, CL-06):
     * refused while any PAID has end_date >= today; otherwise is_deleted = 1 and every PENDING becomes CANCELLED
     * with the reason "Xóa hồ sơ hội viên"; PAID rows are untouched.
     */
    public void softDelete(SessionUser actor, int memberId) {
        // TODO: conn = DBContext.getConnection(); setAutoCommit(false);
        //       memberDAO.lockForUpdate(conn, memberId) null -> NotFoundException; already deleted -> BusinessRuleException;
        //       registrationDAO.hasUnexpiredPaid(conn, memberId, AppClock.today()) -> BusinessRuleException with the reason (KT-11);
        //       memberDAO.markDeleted(conn, memberId);
        //       registrationDAO.cancelAllPending(conn, memberId, actor.getId(), AppConstants.CANCEL_REASON_MEMBER_DELETED);
        //       commit; any error -> DBContext.rollbackQuietly and rethrow.
        // Output: nothing returned; throws an exception when refused.
    }

    /**
     * Restore a soft-deleted profile (CN-16, QT-10). Code, created_at and history are kept; CANCELLED rows stay CANCELLED.
     */
    public void restore(SessionUser actor, int memberId) {
        // TODO: memberDAO.findById null -> NotFoundException; not deleted -> BusinessRuleException; memberDAO.restore.
        // Output: nothing returned; throws an exception on failure.
    }

    /**
     * Expiring-soon list (CN-17, QT-08), display only.
     */
    public List<ExpiringMember> listExpiringSoon() {
        // TODO: membershipStatusService.expiringSoon().
        // Output: list of ExpiringMember sorted by end date ascending.
        return null;
    }
}
