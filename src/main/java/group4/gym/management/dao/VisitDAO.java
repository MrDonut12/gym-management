package group4.gym.management.dao;

import group4.gym.management.dto.VisitFilter;
import group4.gym.management.dto.VisitView;
import group4.gym.management.model.Visit;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Data access for visits (DL-04). No physical deletion; no edit/cancel yet (FUT-01).
 */
public class VisitDAO {

    /**
     * Catch-up close (lazy close, CN-15): switch every OPEN visit past 20:00 on its check-in date to AUTO_CLOSED,
     * with end time = 20:00 on the check-in date and closed_by_system = 1. Only OPEN visits are touched; never processed twice.
     */
    public int autoCloseOverdue(LocalDateTime now) {
        // TODO: UPDATE ... SET status = 'AUTO_CLOSED', check_out_at = DATEADD(HOUR, 20, CAST(check_in_date AS DATETIME2)),
        //       closed_by = NULL, closed_by_system = 1 WHERE status = 'OPEN' AND ? >= (check_in_date 20:00).
        // Output: the number of visits closed.
        return 0;
    }

    /**
     * Find the member's OPEN visit (locks the row inside the check-in transaction to prevent two OPEN visits, QT-03).
     */
    public Visit findOpenByMemberId(Connection connection, int memberId) throws SQLException {
        // TODO: SELECT ... WITH (UPDLOCK, HOLDLOCK) WHERE member_id = ? AND status = 'OPEN'.
        // Output: Visit; null if there is no OPEN visit.
        return null;
    }

    /**
     * Find the member's OPEN visit (plain read, used by screens).
     */
    public Visit findOpenByMemberId(int memberId) {
        // TODO: SELECT ... WHERE member_id = ? AND status = 'OPEN'.
        // Output: Visit; null if there is no OPEN visit.
        return null;
    }

    /**
     * Insert a check-in with status OPEN (CN-14).
     */
    public int insert(Connection connection, Visit visit) throws SQLException {
        // TODO: INSERT member_id, membership_id, check_in_date, check_in_at, status = 'OPEN', created_by.
        //       Preferably also a unique filtered index (member_id) WHERE status = 'OPEN' as a second line of defense.
        // Output: the generated ID.
        return 0;
    }

    /**
     * Manual check-out: OPEN -> COMPLETED with a check on the current status (CN-15).
     */
    public boolean completeIfOpen(int visitId, LocalDateTime checkOutAt, int closedBy) {
        // TODO: UPDATE status = 'COMPLETED', check_out_at = ?, closed_by = ? WHERE id = ? AND status = 'OPEN'.
        // Output: true if exactly 1 row was updated.
        return false;
    }

    /**
     * Find a visit by ID.
     */
    public Visit findById(int id) {
        // TODO: SELECT by id.
        // Output: Visit; null if it does not exist.
        return null;
    }

    /**
     * Look up visit history by member/date/status filter (CN-16).
     */
    public List<VisitView> search(VisitFilter filter) {
        // TODO: SELECT JOIN members, memberships, accounts; WHERE per the filter; ORDER BY check_in_at DESC.
        // Output: list of VisitView.
        return null;
    }

    /**
     * List the visits that are currently OPEN (check-out screen).
     */
    public List<VisitView> findOpenVisits() {
        // TODO: SELECT JOIN ... WHERE status = 'OPEN' ORDER BY check_in_at.
        // Output: list of VisitView.
        return null;
    }

    private Visit mapRow(ResultSet rs) throws SQLException {
        // TODO: Read the columns and assign them to a Visit.
        // Output: Visit.
        return null;
    }
}
