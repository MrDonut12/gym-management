package group4.gym.management.service.registration;

import group4.gym.management.dao.member.MemberDAO;
import group4.gym.management.dao.registration.PackageDAO;
import group4.gym.management.dao.registration.RegistrationDAO;
import group4.gym.management.model.account.SessionUser;
import group4.gym.management.model.member.Member;
import group4.gym.management.model.registration.RegistrationFilter;
import group4.gym.management.model.registration.RegistrationView;
import group4.gym.management.util.Page;
import java.util.List;

/**
 * Registrations (CN-22 to CN-24, QT-02 to QT-04, QT-06, QT-11, QT-13).
 */
public class RegistrationService {

    private final RegistrationDAO registrationDAO = new RegistrationDAO();
    private final PackageDAO packageDAO = new PackageDAO();
    private final MemberDAO memberDAO = new MemberDAO();

    /**
     * Members that may receive a registration: not soft-deleted (CN-22, QT-11).
     */
    public List<Member> findSelectableMembers(String keyword) {
        // TODO: HL-07 keyword; memberDAO.searchActive(keyword, limit).
        // Output: list of Member.
        return null;
    }

    /**
     * Create a PENDING registration (CN-22) in ONE transaction (QT-06):
     * the member must exist and not be soft-deleted (QT-11), the package must be on sale (QT-04, also when POSTed directly),
     * and the snapshot of name/duration/price is copied from the database, never from the form (QT-03).
     *
     * @return ID of the new registration (the servlet redirects to the CN-25 review step)
     */
    public int createPending(SessionUser actor, int memberId, int packageId) {
        // TODO: package = packageDAO.findById; null -> NotFoundException; !active -> BusinessRuleException;
        //       conn = DBContext.getConnection(); setAutoCommit(false);
        //       memberDAO.lockForUpdate(conn, memberId): null -> NotFoundException; deleted -> BusinessRuleException;
        //       registrationDAO.insert(conn, snapshot of package, createdAt = AppClock.now(), createdBy = actor.getId());
        //       commit; any error -> DBContext.rollbackQuietly and rethrow.
        // Output: the registration ID.
        return 0;
    }

    /**
     * Cancel a PENDING registration with a reason (CN-23, QT-02, HL-15). PAID and CANCELLED never change.
     */
    public void cancel(SessionUser actor, int registrationId, String reason) {
        // TODO: HL-15 on the trimmed reason -> ValidationException; registrationDAO.findById null -> NotFoundException;
        //       registrationDAO.cancelIfPending(id, actor.getId(), reason, AppClock.now()) == false
        //       -> BusinessRuleException(AppConstants.MSG_REGISTRATION_NOT_PENDING).
        // Output: nothing returned; throws an exception when refused.
    }

    /**
     * Registration/transaction list (CN-24), 20 rows per page.
     */
    public Page<RegistrationView> search(RegistrationFilter filter) {
        // TODO: range = DateRange.of(filter.getFromDate(), filter.getToDate()) (QT-13 default: current month; HL-16);
        //       axis null -> CREATED_AT; registrationDAO.search(filter, range.getFromInclusive(), range.getToExclusive(), offset, PAGE_SIZE);
        //       registrationDAO.count for the total.
        // Output: Page of RegistrationView.
        return null;
    }
}
