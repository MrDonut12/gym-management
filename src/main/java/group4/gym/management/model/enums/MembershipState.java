package group4.gym.management.model.enums;

/**
 * Business label for the status of one plan registration (section 3.1.2, CN-12).
 * Not stored in the database; derived from the transaction status and start_at/end_exclusive at viewing time.
 */
public enum MembershipState {
    /** Waiting for payment (transaction is PENDING) */
    PENDING_PAYMENT,
    /** Registration cancelled (transaction is CANCELLED) */
    CANCELLED,
    /** Paid but not yet effective (now < start_at) */
    PAID_UPCOMING,
    /** Currently effective (start_at <= now < end_exclusive) */
    ACTIVE,
    /** Expired (now >= end_exclusive) */
    EXPIRED
}
