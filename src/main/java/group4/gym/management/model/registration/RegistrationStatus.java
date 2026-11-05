package group4.gym.management.model.registration;

/**
 * Registration status (DR-04, BR-02). Only PENDING -> PAID and PENDING -> CANCELLED exist;
 * PAID and CANCELLED are final.
 */
public enum RegistrationStatus {
    /** Waiting for payment; never expires by itself, holds no schedule, counts no revenue */
    PENDING,
    /** Full payment received; schedule (start_date/end_date) set; immutable */
    PAID,
    /** Cancelled with a reason (FR-23) or by a member soft-delete (BR-09) */
    CANCELLED
}
