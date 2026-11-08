package group4.gym.management.model.registration;

/**
 * Time axis of the date-range filter in the registration list (FR-24, BR-13).
 */
public enum DateAxis {
    /** Ngày tạo: filter by created_at, every status */
    CREATED_AT,
    /** Ngày thanh toán: filter by paid_at, which keeps PAID only */
    PAID_AT
}
