package group4.gym.management.service;

import group4.gym.management.dao.MembershipDAO;
import group4.gym.management.dao.PlanTypeDAO;
import group4.gym.management.dto.MembershipStatusView;
import group4.gym.management.model.Account;
import group4.gym.management.model.Membership;
import group4.gym.management.model.PaymentTransaction;
import group4.gym.management.model.enums.MembershipState;
import group4.gym.management.model.enums.PaymentMethod;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Plan registration, renewal/repurchase and membership status (CN-10, CN-11, CN-12, QT-18, QT-19, QT-24, QT-25).
 */
public class MembershipService {

    private final MembershipDAO membershipDAO = new MembershipDAO();
    private final PlanTypeDAO planTypeDAO = new PlanTypeDAO();

    /**
     * Create a registration + PENDING transaction for a member who is not soft-deleted (CN-10, CN-11).
     * Stores snapshots of plan name/duration/amount from the current configuration; holds no schedule; ignores any price sent by the client.
     *
     * @return ID of the PENDING transaction just created
     */
    public int createPendingMembership(Account actor, int memberId, int planTypeId, PaymentMethod paymentMethod) {
        // TODO: Check the member exists and is not soft-deleted; load the PlanType; open a Connection, setAutoCommit(false);
        //       membershipDAO.insert + TransactionDAO.insert (PENDING, amount = current price); commit; on error -> rollback.
        // Output: ID of the PENDING transaction.
        return 0;
    }

    /**
     * Determine the start date when confirming PAID (date selection table in section 3.1.3). The date is decided by the system;
     * any date parameter from the client is ignored. Called inside the DB transaction of PaymentService.
     */
    public LocalDateTime determineStartAt(Connection connection, int memberId, LocalDateTime now) throws SQLException {
        // TODO: membershipDAO.findLastOfLiveChain(connection, memberId, now);
        //       null (no PAID record or all PAID records are expired) -> DateTimeUtil.todayStart();
        //       otherwise -> endExclusive of the last membership in the chain.
        // Output: start_at (00:00 Vietnam time).
        return null;
    }

    /**
     * Compute end_exclusive by the rolling calendar from start_at and the duration snapshot (QT-19).
     */
    public LocalDateTime computeEndExclusive(Membership membership, LocalDateTime startAt) {
        // TODO: DateTimeUtil.addDuration(startAt, durationValueSnapshot, durationUnitSnapshot).
        // Output: end_exclusive.
        return null;
    }

    /**
     * List the status of a member's registrations at the moment now (CN-12).
     */
    public List<MembershipStatusView> getMembershipStatuses(int memberId, LocalDateTime now) {
        // TODO: membershipDAO.findByMemberId + matching transactions; for each registration compute state, expiringSoon, lastTrainingDate.
        // Output: list of MembershipStatusView.
        return null;
    }

    /**
     * Determine the status label of a registration (section 3.1.2). An upcoming PAID record is not "active".
     */
    public MembershipState determineState(Membership membership, PaymentTransaction transaction, LocalDateTime now) {
        // TODO: PENDING -> PENDING_PAYMENT; CANCELLED -> CANCELLED;
        //       PAID: now < start_at -> PAID_UPCOMING; start_at <= now < end_exclusive -> ACTIVE; otherwise EXPIRED.
        // Output: MembershipState.
        return null;
    }

    /**
     * Expiry warning (section 3.1.2): an active monthly/6-month/yearly plan with at most 7 calendar days left
     * until end_exclusive and no follow-on PAID plan. Daily Pass is not covered.
     */
    public boolean isExpiringSoon(Membership membership, MembershipState state, LocalDateTime now) {
        // TODO: Consider only state ACTIVE and a unit other than DAY; calendar days between the date of now and the date of end_exclusive <= 7;
        //       membershipDAO.hasPaidStartingAt(memberId, endExclusive) == false.
        // Output: true if a warning is needed.
        return false;
    }
}
