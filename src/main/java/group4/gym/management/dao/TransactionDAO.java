package group4.gym.management.dao;

import group4.gym.management.dto.TransactionFilter;
import group4.gym.management.dto.TransactionView;
import group4.gym.management.model.PaymentTransaction;
import group4.gym.management.model.enums.PaymentMethod;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Data access for payment transactions (DL-06).
 * There is no delete, PAID cancellation, refund or amount editing operation (CN-25).
 */
public class TransactionDAO {

    /**
     * Find a transaction by ID.
     */
    public PaymentTransaction findById(int id) {
        // TODO: SELECT by id.
        // Output: PaymentTransaction; null if it does not exist.
        return null;
    }

    /**
     * Find a transaction by ID inside a DB transaction, locking the row to check its status (QT-26).
     */
    public PaymentTransaction findByIdForUpdate(Connection connection, int id) throws SQLException {
        // TODO: SELECT ... WITH (UPDLOCK, ROWLOCK) WHERE id = ?.
        // Output: PaymentTransaction; null if it does not exist.
        return null;
    }

    /**
     * Find a transaction by plan registration.
     */
    public PaymentTransaction findByMembershipId(int membershipId) {
        // TODO: SELECT ... WHERE membership_id = ?.
        // Output: PaymentTransaction; null if it does not exist.
        return null;
    }

    /**
     * Get all transactions of one member.
     */
    public List<PaymentTransaction> findByMemberId(int memberId) {
        // TODO: SELECT ... WHERE member_id = ? ORDER BY created_at.
        // Output: list of PaymentTransaction.
        return null;
    }

    /**
     * Get the display details of one transaction (with member name, staff names, registration schedule).
     */
    public TransactionView findViewById(int id) {
        // TODO: SELECT JOIN members, accounts, memberships WHERE t.id = ?.
        // Output: TransactionView; null if it does not exist.
        return null;
    }

    /**
     * Filter transactions (CN-24): PENDING/CANCELLED by created_at, PAID by paid_at;
     * range [rangeStart(from), rangeEndExclusive(to)).
     */
    public List<TransactionView> search(TransactionFilter filter) {
        // TODO: Build the WHERE per status with the matching time key, joining the statuses with OR.
        // Output: list of TransactionView.
        return null;
    }

    /**
     * Insert a PENDING transaction (same DB transaction as the Membership, CN-10).
     */
    public int insert(Connection connection, PaymentTransaction transaction) throws SQLException {
        // TODO: INSERT member_id, membership_id, plan_name_snapshot, amount, payment_method, status = 'PENDING', created_at, created_by.
        // Output: the generated ID.
        return 0;
    }

    /**
     * Switch PENDING -> PAID with the condition "still PENDING" (QT-26).
     */
    public boolean markPaidIfPending(Connection connection, int transactionId, PaymentMethod method,
            LocalDateTime paidAt, int confirmedBy, long paidSequence, String proofImagePath) throws SQLException {
        // TODO: UPDATE status = 'PAID', payment_method, paid_at, confirmed_by, paid_sequence, proof_image_path
        //       WHERE id = ? AND status = 'PENDING'.
        // Output: true if exactly 1 row was updated; false if it is no longer PENDING (duplicate request).
        return false;
    }

    /**
     * Switch PENDING -> CANCELLED with the condition "still PENDING" (CN-21, QT-21). PAID is never cancelled.
     */
    public boolean cancelIfPending(Connection connection, int transactionId, LocalDateTime cancelledAt, int cancelledBy)
            throws SQLException {
        // TODO: UPDATE status = 'CANCELLED', cancelled_at, cancelled_by WHERE id = ? AND status = 'PENDING'.
        // Output: true if exactly 1 row was updated.
        return false;
    }

    /**
     * Get the next PAID confirmation sequence number (paid-order, CN-29).
     */
    public long nextPaidSequence(Connection connection) throws SQLException {
        // TODO: Use a SQL Server SEQUENCE (NEXT VALUE FOR) or MAX(paid_sequence) + 1 inside a locked transaction.
        // Output: an increasing sequence number.
        return 0L;
    }

    private PaymentTransaction mapRow(ResultSet rs) throws SQLException {
        // TODO: Read the columns and assign them to a PaymentTransaction.
        // Output: PaymentTransaction.
        return null;
    }
}
