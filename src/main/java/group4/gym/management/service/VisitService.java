package group4.gym.management.service;

import group4.gym.management.dao.MemberDAO;
import group4.gym.management.dao.MembershipDAO;
import group4.gym.management.dao.VisitDAO;
import group4.gym.management.dto.VisitFilter;
import group4.gym.management.dto.VisitView;
import group4.gym.management.model.Account;
import group4.gym.management.model.Visit;
import java.util.List;

/**
 * Visits: check-in, check-out, lazy close, history lookup (CN-13 to CN-16, QT-01, QT-03, QT-15).
 */
public class VisitService {

    private final VisitDAO visitDAO = new VisitDAO();
    private final MemberDAO memberDAO = new MemberDAO();
    private final MembershipDAO membershipDAO = new MembershipDAO();

    /**
     * Catch-up close step (lazy close, CN-15). Must be called before: check-in, check-out, viewing visit history,
     * and member screens that show the visit status.
     */
    public int autoCloseOverdueVisits() {
        // TODO: visitDAO.autoCloseOverdue(DateTimeUtil.now()).
        // Output: the number of visits switched to AUTO_CLOSED.
        return 0;
    }

    /**
     * Check-in (CN-13, CN-14). Conditions: the member exists and is not soft-deleted; a PAID membership with
     * start_at <= now < end_exclusive and now >= paid_at; no OPEN visit (after the catch-up close); 07:00 <= time < 20:00.
     *
     * @return ID of the new OPEN visit
     */
    public int checkIn(Account actor, int memberId) {
        // TODO: autoCloseOverdueVisits(); check the time (DateTimeUtil.isWithinCheckInHours); check the member;
        //       open a DB transaction: visitDAO.findOpenByMemberId(conn) == null; membershipDAO.findUsableForCheckIn != null;
        //       visitDAO.insert(OPEN, createdBy = actor); commit. Each failed condition -> BusinessRuleException with its own message (GT-02).
        // Output: ID of the visit.
        return 0;
    }

    /**
     * Manual check-out (CN-15): only an OPEN visit, before 20:00 on the same check-in day, after the check-in time -> COMPLETED.
     * From 20:00 the visit has already been closed as AUTO_CLOSED, so the request is rejected. Does not change the plan expiry.
     */
    public void checkOut(Account actor, int visitId) {
        // TODO: autoCloseOverdueVisits(); visitDAO.findById; check it is OPEN and check the time;
        //       visitDAO.completeIfOpen(visitId, now, actor.getId()) == true; otherwise BusinessRuleException.
        // Output: nothing returned; throws an exception when rejected.
    }

    /**
     * The member's current OPEN visit (after the catch-up close step).
     */
    public Visit getOpenVisit(int memberId) {
        // TODO: autoCloseOverdueVisits(); visitDAO.findOpenByMemberId(memberId).
        // Output: Visit or null.
        return null;
    }

    /**
     * List the visits that are currently OPEN (check-out screen, after the catch-up close step).
     */
    public List<VisitView> listOpenVisits() {
        // TODO: autoCloseOverdueVisits(); visitDAO.findOpenVisits().
        // Output: list of VisitView.
        return null;
    }

    /**
     * Look up visit history (CN-16), after the catch-up close step.
     */
    public List<VisitView> searchHistory(VisitFilter filter) {
        // TODO: autoCloseOverdueVisits(); visitDAO.search(filter).
        // Output: list of VisitView.
        return null;
    }
}
