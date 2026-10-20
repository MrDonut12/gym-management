package group4.gym.management.dao;

import group4.gym.management.model.Membership;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Data access for plan registrations (DL-03).
 * Methods that take a Connection are called inside a database transaction managed by the service (QT-26).
 */
public class MembershipDAO {

    /**
     * Find a registration by ID.
     */
    public Membership findById(int id) {
        // TODO: SELECT by id.
        // Output: Membership; null if it does not exist.
        return null;
    }

    /**
     * Get all registrations of a member (history, current, upcoming, PENDING, cancelled).
     */
    public List<Membership> findByMemberId(int memberId) {
        // TODO: SELECT ... WHERE member_id = ? ORDER BY start_at, created_at.
        // Output: list of Membership.
        return null;
    }

    /**
     * Insert a registration (in the same DB transaction as creating the PENDING PaymentTransaction, CN-10).
     */
    public int insert(Connection connection, Membership membership) throws SQLException {
        // TODO: INSERT the name/duration snapshot, created_at, created_by; start_at/end_exclusive = NULL.
        // Output: the generated ID.
        return 0;
    }

    /**
     * Find the last membership of the PAID chain that is still active or upcoming at the moment now (QT-18).
     * Expired history is not a join point for a new schedule.
     */
    public Membership findLastOfLiveChain(Connection connection, int memberId, LocalDateTime now) throws SQLException {
        // TODO: SELECT TOP 1 m.* JOIN PAID transactions WHERE member_id = ? AND end_exclusive > ? ORDER BY end_exclusive DESC.
        // Output: the last Membership of the chain; null if there is no PAID record or all PAID records are expired.
        return null;
    }

    /**
     * Write the official schedule when confirming PAID (same DB transaction as switching the status to PAID).
     */
    public boolean updateSchedule(Connection connection, int membershipId, LocalDateTime startAt, LocalDateTime endExclusive)
            throws SQLException {
        // TODO: UPDATE start_at, end_exclusive WHERE id = ? AND start_at IS NULL.
        // Output: true if exactly 1 row was updated.
        return false;
    }

    /**
     * Find the PAID membership that is valid for check-in at the moment at (CN-13):
     * start_at <= at < end_exclusive and paid_at <= at.
     */
    public Membership findUsableForCheckIn(Connection connection, int memberId, LocalDateTime at) throws SQLException {
        // TODO: SELECT m.* JOIN transactions WHERE status = 'PAID' AND start_at <= ? AND end_exclusive > ? AND paid_at <= ?.
        // Output: Membership; null if there is no valid membership.
        return null;
    }

    /**
     * Whether a PAID membership starts exactly at the given moment (turns off the expiry warning, section 3.1.2).
     */
    public boolean hasPaidStartingAt(int memberId, LocalDateTime startAt) {
        // TODO: SELECT COUNT(*) ... WHERE member_id = ? AND start_at = ? (PAID transaction).
        // Output: true if a follow-on membership exists.
        return false;
    }

    private Membership mapRow(ResultSet rs) throws SQLException {
        // TODO: Read the columns and assign them to a Membership.
        // Output: Membership.
        return null;
    }
}
