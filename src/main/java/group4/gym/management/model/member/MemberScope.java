package group4.gym.management.model.member;

/**
 * Profile filter of the member list (FR-11). DELETED and ALL are MANAGER only (BR-11).
 */
public enum MemberScope {
    /** Managed: not soft-deleted (default) */
    ACTIVE,
    /** Deleted: soft-deleted only */
    DELETED,
    /** All profiles */
    ALL
}
