package group4.gym.management.model.account;

/**
 * Staff account status (DL-01). Accounts are never deleted; LOCKED replaces deletion (QT-16).
 */
public enum AccountStatus {
    /** Account is active */
    ACTIVE,
    /** Account is locked: login is blocked and the session is invalidated on the next request (CN-03, CN-08) */
    LOCKED
}
