package group4.gym.management.model.member;

/**
 * Profile filter of the member list (CN-11). DELETED and ALL are MANAGER only (QT-11).
 */
public enum MemberScope {
    /** Đang quản lý: not soft-deleted (default) */
    ACTIVE,
    /** Đã xóa: soft-deleted only */
    DELETED,
    /** Tất cả */
    ALL
}
