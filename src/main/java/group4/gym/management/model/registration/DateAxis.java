package group4.gym.management.model.registration;

/**
 * Time axis of the date-range filter in the registration list.
 */
public enum DateAxis {
    /** Created date: filter by created_at, every status */
    CREATED_AT,
    /** Paid date: filter by paid_at, which keeps PAID only */
    PAID_AT
}
