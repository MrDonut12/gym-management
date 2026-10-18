package group4.gym.management.service;

import group4.gym.management.dao.MembershipDAO;
import group4.gym.management.dao.TransactionDAO;
import group4.gym.management.dto.TransactionFilter;
import group4.gym.management.dto.TransactionView;
import group4.gym.management.model.Account;
import group4.gym.management.model.enums.PaymentMethod;
import java.util.List;

/**
 * Payments and transactions (CN-21 to CN-25, QT-26).
 */
public class PaymentService {

    private final TransactionDAO transactionDAO = new TransactionDAO();
    private final MembershipDAO membershipDAO = new MembershipDAO();
    private final MembershipService membershipService = new MembershipService();

    /**
     * Confirm a PENDING transaction as PAID (CN-21, CN-22). Atomic operation (QT-26):
     * switch to PAID + store paid_at/confirmer/confirmation order + set the schedule in one DB transaction.
     * A duplicate request or a transaction that is no longer PENDING is rejected without changing established data.
     *
     * @param proofImagePath bank transfer proof image (optional, may be null)
     */
    public void confirmPayment(Account actor, int transactionId, PaymentMethod paymentMethod, String proofImagePath) {
        // TODO: Open a Connection, setAutoCommit(false);
        //       transactionDAO.findByIdForUpdate -> must be PENDING; member must not be soft-deleted;
        //       now = DateTimeUtil.now(); startAt = membershipService.determineStartAt(conn, memberId, now);
        //       endExclusive = membershipService.computeEndExclusive(...);
        //       transactionDAO.markPaidIfPending(... nextPaidSequence ...) == true; membershipDAO.updateSchedule(...) == true;
        //       commit; on any error -> DBContext.rollbackQuietly + throw BusinessRuleException.
        // Output: nothing returned; throws an exception when rejected.
    }

    /**
     * Cancel a PENDING transaction together with its registration (CN-21, QT-21). PAID is never cancelled.
     */
    public void cancelPendingTransaction(Account actor, int transactionId) {
        // TODO: transactionDAO.cancelIfPending inside a DB transaction; false -> BusinessRuleException (no longer PENDING).
        // Output: nothing returned; throws an exception when rejected.
    }

    /**
     * Warning (non-blocking) when confirming a Daily Pass that starts a new chain at or after 20:00 (section 3.1.3).
     */
    public boolean shouldWarnLateDailyPass(int transactionId) {
        // TODO: The registration is a Daily Pass, there is no PAID chain that is active/upcoming, and the current time is >= 20:00.
        // Output: true if the warning must be shown on the confirmation screen.
        return false;
    }

    /**
     * Look up/filter transactions (CN-23, CN-24).
     */
    public List<TransactionView> searchTransactions(TransactionFilter filter) {
        // TODO: Check fromDate <= toDate; transactionDAO.search(filter).
        // Output: list of TransactionView.
        return null;
    }

    /**
     * Details of one transaction (CN-23).
     */
    public TransactionView getTransactionDetail(int transactionId) {
        // TODO: transactionDAO.findViewById; not found -> BusinessRuleException.
        // Output: TransactionView.
        return null;
    }
}
