package group4.gym.management.service.payment;

import group4.gym.management.dao.payment.ReportDAO;
import group4.gym.management.model.payment.MemberOverview;
import group4.gym.management.model.payment.RevenueGranularity;
import group4.gym.management.model.payment.RevenueRow;
import group4.gym.management.service.member.MembershipStatusService;
import group4.gym.management.util.DateRange;
import java.util.List;

/**
 * Reports (CN-26 to CN-28, QT-12, QT-13). Ranges come from DateRange: [from 00:00, to+1 00:00), default current month.
 */
public class ReportService {

    private final ReportDAO reportDAO = new ReportDAO();
    private final MembershipStatusService membershipStatusService = new MembershipStatusService();

    /**
     * Revenue per day or month (CN-26).
     */
    public List<RevenueRow> revenueByPeriod(DateRange range, RevenueGranularity granularity) {
        // TODO: granularity null -> DAY; reportDAO.revenueByPeriod(range.getFromInclusive(), range.getToExclusive(), granularity);
        //       set each row's label from periodStart (dd/MM/yyyy or MM/yyyy).
        // Output: list of RevenueRow.
        return null;
    }

    /**
     * Revenue per package (CN-27), same range as CN-26. A renamed package stays one row (grouped by package_id).
     */
    public List<RevenueRow> revenueByPackage(DateRange range) {
        // TODO: reportDAO.revenueByPackage; sharePercent = revenue * 100.0 / total (0 when total = 0).
        // Output: list of RevenueRow; its total equals the CN-26 total for the same range (KT-24).
        return null;
    }

    /**
     * Total row at the end of a revenue table.
     */
    public RevenueRow totalOf(List<RevenueRow> rows) {
        // TODO: Sum transactionCount and revenue; label = "Total"; sharePercent = 100 when revenue > 0.
        // Output: the total RevenueRow.
        return null;
    }

    /**
     * Member overview, the MANAGER home page (CN-28).
     */
    public MemberOverview memberOverview(DateRange range) {
        // TODO: activeProfiles = reportDAO.countMembers(false); deletedProfiles = countMembers(true);
        //       membersWithActivePackage = countMembersWithActivePackage(AppClock.today());
        //       expiringSoon = membershipStatusService.expiringSoon().size();
        //       newMembers = countNewMembers(range.getFromInclusive(), range.getToExclusive()).
        // Output: MemberOverview.
        return null;
    }
}
