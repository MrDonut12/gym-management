package group4.gym.management.model.enums;

/**
 * Staff account status (DL-07). Independent from the soft-delete flag.
 */
public enum AccountStatus {
    /** Account is active */
    ACTIVE,
    /** Account is locked: login is blocked and the session is invalidated on the next protected request */
    LOCKED
}
