package group4.gym.management.model.enums;

/**
 * Plan type duration unit (DL-02). Fixed catalog: 1 day / 1 month / 6 months / 1 year.
 */
public enum DurationUnit {
    /** Days */
    DAY,
    /** Months (calendar addition, not converted to 30 days) */
    MONTH,
    /** Years (calendar addition, not converted to 365 days) */
    YEAR
}
