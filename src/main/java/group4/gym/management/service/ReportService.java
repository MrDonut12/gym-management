package group4.gym.management.service;

import group4.gym.management.dao.ReportDAO;
import group4.gym.management.dto.MemberStatistics;
import group4.gym.management.dto.PlanRevenueSummary;
import group4.gym.management.dto.PurchaseStatistics;
import group4.gym.management.dto.RevenueGranularity;
import group4.gym.management.dto.RevenueSummary;
import java.time.LocalDate;
import java.util.List;

/**
 * Reports and statistics (CN-26 to CN-29, QT-13, QT-23).
 * Date range: from 00:00:00 on the start date up to (but excluding) 00:00:00 on the day after the end date.
 */
public class ReportService {

    private final ReportDAO reportDAO = new ReportDAO();

    /**
     * Revenue per day/month (CN-26).
     */
    public List<RevenueSummary> getRevenue(LocalDate fromDate, LocalDate toDate, RevenueGranularity granularity) {
        // TODO: Validate the date range; convert with DateTimeUtil.rangeStart/rangeEndExclusive; reportDAO.sumRevenue.
        // Output: list of RevenueSummary.
        return null;
    }

    /**
     * Total revenue within the range (CN-26).
     */
    public long getTotalRevenue(LocalDate fromDate, LocalDate toDate) {
        // TODO: reportDAO.sumTotalRevenue with the converted range.
        // Output: the total amount.
        return 0L;
    }

    /**
     * Plan revenue (CN-27).
     */
    public List<PlanRevenueSummary> getPlanRevenue(LocalDate fromDate, LocalDate toDate) {
        // TODO: reportDAO.sumRevenueByPlan with the converted range.
        // Output: list of PlanRevenueSummary.
        return null;
    }

    /**
     * Total members (including soft-deleted, filterable by status) and new members in the period (CN-28).
     */
    public MemberStatistics getMemberStatistics(LocalDate fromDate, LocalDate toDate, Boolean deletedFilter) {
        // TODO: reportDAO.countMembers(null/true/false) and countNewMembers; assign them to MemberStatistics.
        // Output: MemberStatistics.
        return null;
    }

    /**
     * New registrations and renewals/repurchases in the period (CN-29).
     */
    public PurchaseStatistics getPurchaseStatistics(LocalDate fromDate, LocalDate toDate) {
        // TODO: reportDAO.countNewRegistrations and countRenewals; assign them to PurchaseStatistics.
        // Output: PurchaseStatistics.
        return null;
    }
}
