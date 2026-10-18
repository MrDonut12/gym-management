package group4.gym.management.model.enums;

/**
 * Visit status (section 3.1.4). CANCELLED belongs to FUT-01 and is therefore not declared.
 */
public enum VisitStatus {
    /** Checked in, not yet ended */
    OPEN,
    /** Manually checked out before 20:00 on the same day */
    COMPLETED,
    /** Closed by the system (lazy close); conventional end time is 20:00 on the check-in day */
    AUTO_CLOSED
}
