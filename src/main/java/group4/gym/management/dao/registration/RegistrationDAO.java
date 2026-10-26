package group4.gym.management.dao.registration;

import group4.gym.management.model.registration.PaymentMethod;
import group4.gym.management.model.registration.Registration;
import group4.gym.management.model.registration.RegistrationFilter;
import group4.gym.management.model.registration.RegistrationView;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Data access for registrations (table registrations, DL-04; owner TV3, payment columns written for TV4).
 * No delete, no update of PAID/CANCELLED rows (QT-02, QT-16). Every status change is conditional:
 * UPDATE ... WHERE id = ? AND status = 'PENDING' (0 rows = "no longer pending").
 * Every query uses PreparedStatement parameters (CL-02). Methods without a Connection open their own with
 * DBContext.getConnection() and wrap SQLException in AppException; methods with a Connection run inside the
 * caller's transaction (SRS 9.1) and throw SQLException.
 */
public class RegistrationDAO {

    /**
     * Insert a PENDING registration with its snapshot (CN-22, inside the QT-06 transaction).
     */
    public int insert(Connection conn, Registration registration) throws SQLException {
        // TODO: INSERT member_id, package_id, package_name_snapshot, duration_months_snapshot, price_snapshot,
        //       status = 'PENDING', created_at, created_by; every payment/cancel column NULL.
        // Output: the generated ID.
        return 0;
    }

    /**
     * Find a registration by ID.
     */
    public Registration findById(int id) {
        // TODO: SELECT ... FROM registrations WHERE id = ?.
        // Output: Registration; null if it does not exist.
        return null;
    }

    /**
     * Re-read a registration inside a transaction, after the member row is locked (CN-25, QT-06).
     */
    public Registration findById(Connection conn, int id) throws SQLException {
        // TODO: SELECT ... FROM registrations WHERE id = ? using conn.
        // Output: Registration; null if it does not exist.
        return null;
    }

    /**
     * Registration with member display data (CN-25 review step).
     */
    public RegistrationView findViewById(int id) {
        // TODO: SELECT r.*, m.full_name, m.phone, m.is_deleted FROM registrations r JOIN members m ON m.id = r.member_id WHERE r.id = ?.
        // Output: RegistrationView; null if it does not exist.
        return null;
    }

    /**
     * Every registration of a member, every status, newest first (shared interface SRS 9.1, used by TV2 for CN-13).
     */
    public List<Registration> findByMember(int memberId) {
        // TODO: SELECT ... WHERE member_id = ? ORDER BY created_at DESC, id DESC.
        // Output: list of Registration.
        return null;
    }

    /**
     * One page of the registration list (CN-24).
     * Axis CREATED_AT: created_at in [from, to+1); axis PAID_AT: paid_at in [from, to+1), which keeps PAID only (QT-13).
     *
     * @param fromInclusive DateRange.getFromInclusive()
     * @param toExclusive   DateRange.getToExclusive()
     */
    public List<RegistrationView> search(RegistrationFilter filter, LocalDateTime fromInclusive, LocalDateTime toExclusive,
            int offset, int limit) {
        // TODO: JOIN members; WHERE [status = ?] [package_id = ?] [member id = ? OR phone = ?] AND <axis column> >= ? AND < ?;
        //       the axis column comes from a fixed switch on DateAxis, never from user text (CL-02);
        //       ORDER BY <axis column> DESC OFFSET ? ROWS FETCH NEXT ? ROWS ONLY.
        // Output: list of RegistrationView.
        return null;
    }

    /**
     * Total rows of search() for the same criteria (pagination).
     */
    public int count(RegistrationFilter filter, LocalDateTime fromInclusive, LocalDateTime toExclusive) {
        // TODO: SELECT COUNT(*) with the same WHERE as search().
        // Output: number of matching registrations.
        return 0;
    }

    /**
     * Cancel one PENDING registration (CN-23, QT-02).
     */
    public boolean cancelIfPending(int id, int cancelledBy, String reason, LocalDateTime cancelledAt) {
        // TODO: UPDATE registrations SET status = 'CANCELLED', cancelled_at = ?, cancelled_by = ?, cancel_reason = ?
        //       WHERE id = ? AND status = 'PENDING'.
        // Output: true if exactly 1 row was updated; false if it is no longer PENDING.
        return false;
    }

    /**
     * Cancel every PENDING registration of a member (shared interface SRS 9.1, used by TV2 in the CN-15 transaction, QT-09).
     */
    public int cancelAllPending(Connection conn, int memberId, int byId, String reason) throws SQLException {
        // TODO: UPDATE registrations SET status = 'CANCELLED', cancelled_at = AppClock.now(), cancelled_by = ?, cancel_reason = ?
        //       WHERE member_id = ? AND status = 'PENDING'.
        // Output: the number of registrations cancelled.
        return 0;
    }

    /**
     * Whether the member still has a PAID registration with end_date >= today, active or upcoming
     * (shared interface SRS 9.1, used by TV2 in the CN-15 transaction, QT-09).
     */
    public boolean hasUnexpiredPaid(Connection conn, int memberId, LocalDate today) throws SQLException {
        // TODO: SELECT COUNT(*) FROM registrations WHERE member_id = ? AND status = 'PAID' AND end_date >= ?.
        // Output: true if soft-delete must be refused.
        return false;
    }

    /**
     * Largest end_date of the member's PAID registrations (QT-05), read inside the CN-25 transaction.
     */
    public LocalDate findMaxPaidEndDate(Connection conn, int memberId) throws SQLException {
        // TODO: SELECT MAX(end_date) FROM registrations WHERE member_id = ? AND status = 'PAID'.
        // Output: the date; null if the member has no PAID registration.
        return null;
    }

    /**
     * Same as above, outside a transaction (CN-25 review step only).
     */
    public LocalDate findMaxPaidEndDate(int memberId) {
        // TODO: Same query with a new connection.
        // Output: the date; null if the member has no PAID registration.
        return null;
    }

    /**
     * PENDING -> PAID with the schedule, in one statement (CN-25, QT-02, used by PaymentService of TV4).
     */
    public boolean markPaidIfPending(Connection conn, int id, PaymentMethod method, LocalDateTime paidAt, int confirmedBy,
            LocalDate startDate, LocalDate endDate) throws SQLException {
        // TODO: UPDATE registrations SET status = 'PAID', payment_method = ?, paid_at = ?, confirmed_by = ?, start_date = ?, end_date = ?
        //       WHERE id = ? AND status = 'PENDING'.
        // Output: true if exactly 1 row was updated; false if it is no longer PENDING.
        return false;
    }

    private Registration mapRow(ResultSet rs) throws SQLException {
        // TODO: Read the columns and assign them to a Registration (nullable DATE/DATETIME2/INT columns stay null).
        // Output: Registration.
        return null;
    }
}
