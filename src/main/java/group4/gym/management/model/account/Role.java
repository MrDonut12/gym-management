package group4.gym.management.model.account;

/**
 * The three fixed roles. Each account has exactly one role; no dynamic roles.
 * Members have no login account.
 */
public enum Role {
    /** Manages staff accounts only; no member/package/payment permissions */
    ADMIN,
    /** All business operations: members (including soft-delete/restore), packages, registrations, payments, reports */
    MANAGER,
    /** Front-desk operations: create/edit/search members, create/cancel registrations, confirm payments */
    RECEPTIONIST
}
