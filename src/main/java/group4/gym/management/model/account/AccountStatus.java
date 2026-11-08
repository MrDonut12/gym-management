package group4.gym.management.model.account;

/**
 * Staff account status. Accounts are never deleted; LOCKED replaces deletion.
 */
public enum AccountStatus {
    /** Account is active */
    ACTIVE,
    /** Account is locked: login is blocked and the session is invalidated on the next request */
    LOCKED
}
