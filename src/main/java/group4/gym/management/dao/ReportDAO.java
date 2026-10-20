package group4.gym.management.dao;

import group4.gym.management.dto.PlanRevenueSummary;
import group4.gym.management.dto.RevenueGranularity;
import group4.gym.management.dto.RevenueSummary;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Aggregate queries for reports (CN-26 to CN-29).
 * Every time range is [fromInclusive, toExclusive); revenue counts only PAID transactions by paid_at.
 */
public class ReportDAO {

    /**
     * Revenue per day/month (CN-26).
     */
    public List<RevenueSummary> sumRevenue(LocalDateTime fromInclusive, LocalDateTime toExclusive, RevenueGranularity granularity) {
        // TODO: SELECT the period (CAST(paid_at AS DATE) or month), COUNT(*), SUM(amount)
        //       WHERE status = 'PAID' AND paid_at >= ? AND paid_at < ? GROUP BY period ORDER BY period.
        // Output: list of RevenueSummary.
        return null;
    }

    /**
     * Total revenue within the range (CN-26).
     */
    public long sumTotalRevenue(LocalDateTime fromInclusive, LocalDateTime toExclusive) {
        // TODO: SELECT COALESCE(SUM(amount), 0) WHERE status = 'PAID' AND paid_at within the range.
        // Output: the total amount.
        return 0L;
    }

    /**
     * Plan revenue grouped by plan name snapshot (CN-27).
     */
    public List<PlanRevenueSummary> sumRevenueByPlan(LocalDateTime fromInclusive, LocalDateTime toExclusive) {
        // TODO: GROUP BY plan_name_snapshot with the conditions PAID + paid_at within the range.
        // Output: list of PlanRevenueSummary.
        return null;
    }

    /**
     * Count member profiles (CN-28). deletedFilter: null = all, true = soft-deleted, false = not soft-deleted.
     */
    public int countMembers(Boolean deletedFilter) {
        // TODO: SELECT COUNT(*) FROM members [WHERE is_deleted = ?].
        // Output: number of profiles.
        return 0;
    }

    /**
     * Count new members by Members.created_at within the range (CN-28), regardless of purchases or soft-deletion.
     */
    public int countNewMembers(LocalDateTime fromInclusive, LocalDateTime toExclusive, Boolean deletedFilter) {
        // TODO: SELECT COUNT(*) WHERE created_at >= ? AND created_at < ? [AND is_deleted = ?].
        // Output: number of new members.
        return 0;
    }

    /**
     * Count new registrations (CN-29): a member's first PAID record by paid_sequence with paid_at within the period.
     */
    public int countNewRegistrations(LocalDateTime fromInclusive, LocalDateTime toExclusive) {
        // TODO: Use ROW_NUMBER() OVER (PARTITION BY member_id ORDER BY paid_sequence) over the whole PAID history,
        //       keep rn = 1 and paid_at within the period.
        // Output: number of new registrations.
        return 0;
    }

    /**
     * Count renewals/repurchases (CN-29): PAID records with order > 1 and paid_at within the period.
     */
    public int countRenewals(LocalDateTime fromInclusive, LocalDateTime toExclusive) {
        // TODO: Same as countNewRegistrations but keep rn > 1.
        // Output: number of renewals.
        return 0;
    }
}
