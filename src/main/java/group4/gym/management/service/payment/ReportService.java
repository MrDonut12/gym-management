package group4.gym.management.service.payment;

import group4.gym.management.dao.payment.ReportDAO;
import group4.gym.management.model.payment.MemberOverview;
import group4.gym.management.model.payment.RevenueGranularity;
import group4.gym.management.model.payment.RevenueRow;
import group4.gym.management.service.member.MembershipStatusService;
import group4.gym.management.util.AppClock;
import group4.gym.management.util.DateRange;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Reports. Ranges come from DateRange: [from 00:00, to+1 00:00), default current month.
 * Access (MANAGER only) is enforced by AuthFilter on the /manager prefix.
 */
public class ReportService {

    private static final DateTimeFormatter DAY_LABEL = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private static final DateTimeFormatter MONTH_LABEL = DateTimeFormatter.ofPattern("MM/yyyy");

    private final ReportDAO reportDAO = new ReportDAO();
    private final MembershipStatusService membershipStatusService = new MembershipStatusService();

    /**
     * Revenue per day or month.
     */
    public List<RevenueRow> revenueByPeriod(DateRange range, RevenueGranularity granularity) {
        if (granularity == null) {
            granularity = RevenueGranularity.DAY;
        }
        DateRange safeRange = rangeOrDefault(range);
        List<RevenueRow> rows = reportDAO.revenueByPeriod(
                safeRange.getFromInclusive(), safeRange.getToExclusive(), granularity);
        DateTimeFormatter formatter = granularity == RevenueGranularity.MONTH ? MONTH_LABEL : DAY_LABEL;
        for (RevenueRow row : rows) {
            row.setLabel(row.getPeriodStart().format(formatter));
        }
        return rows;
    }

    /**
     * Revenue per package, same range as the period report. A renamed package stays one row (grouped by package_id).
     */
    public List<RevenueRow> revenueByPackage(DateRange range) {
        DateRange safeRange = rangeOrDefault(range);
        List<RevenueRow> rows = reportDAO.revenueByPackage(
                safeRange.getFromInclusive(), safeRange.getToExclusive());
        long total = 0;
        for (RevenueRow row : rows) {
            total += row.getRevenue();
        }
        for (RevenueRow row : rows) {
            row.setSharePercent(total == 0 ? 0 : row.getRevenue() * 100.0 / total);
        }
        return rows;
    }

    /**
     * Total row at the end of a revenue table.
     */
    public RevenueRow totalOf(List<RevenueRow> rows) {
        int transactionCount = 0;
        long revenue = 0;
        if (rows != null) {
            for (RevenueRow row : rows) {
                transactionCount += row.getTransactionCount();
                revenue += row.getRevenue();
            }
        }
        RevenueRow total = new RevenueRow();
        total.setLabel("Total");
        total.setTransactionCount(transactionCount);
        total.setRevenue(revenue);
        total.setSharePercent(revenue > 0 ? 100 : 0);
        return total;
    }

    /**
     * Member overview, the MANAGER home page.
     */
    public MemberOverview memberOverview(DateRange range) {
        DateRange safeRange = rangeOrDefault(range);
        MemberOverview overview = new MemberOverview();
        overview.setActiveProfiles(reportDAO.countMembers(false));
        overview.setDeletedProfiles(reportDAO.countMembers(true));
        overview.setMembersWithActivePackage(reportDAO.countMembersWithActivePackage(AppClock.today()));
        overview.setExpiringSoon(membershipStatusService.expiringSoon().size());
        overview.setNewMembers(reportDAO.countNewMembers(
                safeRange.getFromInclusive(), safeRange.getToExclusive()));
        return overview;
    }

    /**
     * Falls back to the current month when no range is given.
     */
    private DateRange rangeOrDefault(DateRange range) {
        return range == null ? DateRange.currentMonth() : range;
    }
}
