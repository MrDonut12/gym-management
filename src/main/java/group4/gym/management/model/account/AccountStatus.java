package group4.gym.management.model.account;

/**
 * Staff account status (DR-01). Accounts are never deleted; LOCKED replaces deletion (BR-16).
 */
public enum AccountStatus {
    /** Account is active */
    ACTIVE,
    /** Account is locked: login is blocked and the session is invalidated on the next request (FR-03, FR-08) */
    LOCKED
}
