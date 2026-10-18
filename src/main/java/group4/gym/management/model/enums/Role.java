package group4.gym.management.model.enums;

/**
 * The five fixed roles (SRS 2.2, CN-05). Each account has exactly one role; no dynamic roles.
 */
public enum Role {
    /** Manages accounts and fixed role assignment */
    ADMINISTRATOR,
    /** Monitors operations and manages plan types */
    OWNER,
    /** Runs front-desk operations; manages Receptionist/Accountant accounts */
    MANAGER,
    /** Handles members, plan registration, payments and visits */
    RECEPTIONIST,
    /** Looks up transactions and revenue */
    ACCOUNTANT
}
