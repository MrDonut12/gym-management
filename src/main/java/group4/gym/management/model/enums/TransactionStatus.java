package group4.gym.management.model.enums;

/**
 * Payment transaction status (section 3.1.5).
 */
public enum TransactionStatus {
    /** Waiting for full payment confirmation; grants no access and holds no schedule */
    PENDING,
    /** Full payment confirmed; cannot be cancelled, refunded or adjusted */
    PAID,
    /** Cancelled (only reachable from PENDING) */
    CANCELLED
}
