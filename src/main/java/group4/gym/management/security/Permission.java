package group4.gym.management.security;

/**
 * Must-level business actions that require permission control according to the RBAC matrix (section 2.3, CN-34).
 * Actions belonging to Should/Should (B)/FUT are not declared in this baseline.
 */
public enum Permission {
    /** Change own password (every role, CN-36) */
    CHANGE_OWN_PASSWORD,
    /** Reset another account's password (Administrator only, CN-36) */
    RESET_OTHER_PASSWORD,
    /** View the accounts list within the management scope (Administrator, Manager) */
    VIEW_ACCOUNTS,
    /** Create/edit accounts within scope (Administrator: any role; Manager: Receptionist/Accountant) */
    MANAGE_ACCOUNT,
    /** Lock/unlock accounts within scope */
    LOCK_UNLOCK_ACCOUNT,
    /** Assign/change roles (Administrator only) */
    ASSIGN_ROLE,
    /** Soft-delete accounts (Administrator only, self-deletion forbidden) */
    SOFT_DELETE_ACCOUNT,
    /** View/search members and membership status (Owner, Manager, Receptionist) */
    VIEW_MEMBERS,
    /** Create/update member profiles (Manager, Receptionist) */
    CREATE_UPDATE_MEMBER,
    /** Soft-delete members (Owner, Manager) */
    SOFT_DELETE_MEMBER,
    /** View the plan type catalog (Owner, Manager; Receptionist reads it within the registration flow) */
    VIEW_PLAN_TYPES,
    /** Create plan types (Owner only) */
    CREATE_PLAN_TYPE,
    /** Edit plan type name and price (Owner, Manager) */
    UPDATE_PLAN_TYPE,
    /** Register/renew - create PENDING (Manager, Receptionist) */
    CREATE_MEMBERSHIP,
    /** Confirm payment as PAID (Manager, Receptionist) */
    CONFIRM_PAYMENT,
    /** Cancel PENDING transactions and the related registration (Manager, Receptionist) */
    CANCEL_PENDING_TRANSACTION,
    /** View/filter the transaction list (Owner, Manager, Receptionist, Accountant) */
    VIEW_TRANSACTIONS,
    /** Check-in/check-out (Manager, Receptionist) */
    CHECK_IN_OUT,
    /** View visit history (Owner, Manager, Receptionist) */
    VIEW_VISIT_HISTORY,
    /** View revenue reports (Owner, Manager, Accountant) */
    VIEW_REVENUE_REPORT,
    /** View member and purchase statistics (Owner, Manager) */
    VIEW_MEMBER_REPORT
}
