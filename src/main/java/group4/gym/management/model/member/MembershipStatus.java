package group4.gym.management.model.member;

/**
 * Membership status of a member at "today". Not stored in the database; derived from PAID registrations.
 * Priority order: ACTIVE -> UPCOMING -> EXPIRED -> NONE.
 */
public enum MembershipStatus {
    /** Active: some PAID has start_date <= today <= end_date */
    ACTIVE,
    /** Upcoming: only PAID with start_date > today */
    UPCOMING,
    /** Expired: every PAID has end_date < today */
    EXPIRED,
    /** No package: no PAID registration */
    NONE
}
