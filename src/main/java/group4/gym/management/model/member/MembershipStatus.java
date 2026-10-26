package group4.gym.management.model.member;

/**
 * Membership status of a member at "today" (QT-07). Not stored in the database; derived from PAID registrations.
 * Priority order: ACTIVE -> UPCOMING -> EXPIRED -> NONE.
 */
public enum MembershipStatus {
    /** Đang hiệu lực: some PAID has start_date <= today <= end_date */
    ACTIVE,
    /** Chờ hiệu lực: only PAID with start_date > today */
    UPCOMING,
    /** Hết hạn: every PAID has end_date < today */
    EXPIRED,
    /** Chưa có gói: no PAID registration */
    NONE
}
