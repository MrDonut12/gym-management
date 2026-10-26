package group4.gym.management.dao.payment;

import group4.gym.management.model.payment.RevenueGranularity;
import group4.gym.management.model.payment.RevenueRow;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Aggregate queries for the reports (CN-26 to CN-28). Every range is [fromInclusive, toExclusive) (QT-13).
 * Revenue = SUM(price_snapshot) of PAID registrations with paid_at in the range; PENDING/CANCELLED excluded (QT-12).
 * Every query uses PreparedStatement parameters (CL-02). Methods open their own connection with
 * DBContext.getConnection() and wrap SQLException in AppException.
 */
public class ReportDAO {

    /**
     * Revenue per day or month (CN-26). Periods without revenue may be omitted.
     */
    public List<RevenueRow> revenueByPeriod(LocalDateTime fromInclusive, LocalDateTime toExclusive, RevenueGranularity granularity) {
        // TODO: SELECT period = CAST(paid_at AS DATE) (DAY) or DATEFROMPARTS(YEAR(paid_at), MONTH(paid_at), 1) (MONTH),
        //       COUNT(*), SUM(CAST(price_snapshot AS BIGINT)) WHERE status = 'PAID' AND paid_at >= ? AND paid_at < ?
        //       GROUP BY period ORDER BY period. The SQL fragment comes from a fixed switch on granularity (CL-02).
        // Output: list of RevenueRow (periodStart, transactionCount, revenue).
        return null;
    }

    /**
     * Revenue per package (CN-27): grouped by package_id, labelled with the CURRENT package name (QT-12).
     */
    public List<RevenueRow> revenueByPackage(LocalDateTime fromInclusive, LocalDateTime toExclusive) {
        // TODO: SELECT r.package_id, p.name, COUNT(*), SUM(CAST(r.price_snapshot AS BIGINT)) FROM registrations r
        //       JOIN packages p ON p.id = r.package_id WHERE r.status = 'PAID' AND r.paid_at >= ? AND r.paid_at < ?
        //       GROUP BY r.package_id, p.name ORDER BY revenue DESC.
        // Output: list of RevenueRow (packageId, label, transactionCount, revenue); sharePercent is set by the service.
        return null;
    }

    /**
     * Count profiles by soft-delete flag (CN-28).
     */
    public int countMembers(boolean deleted) {
        // TODO: SELECT COUNT(*) FROM members WHERE is_deleted = ?.
        // Output: number of profiles.
        return 0;
    }

    /**
     * Members with a PAID registration covering today, i.e. membership status ACTIVE (CN-28, QT-07).
     */
    public int countMembersWithActivePackage(LocalDate today) {
        // TODO: SELECT COUNT(DISTINCT member_id) FROM registrations WHERE status = 'PAID' AND start_date <= ? AND end_date >= ?.
        // Output: number of members.
        return 0;
    }

    /**
     * New members by created_at in the range, including profiles soft-deleted later (CN-28).
     */
    public int countNewMembers(LocalDateTime fromInclusive, LocalDateTime toExclusive) {
        // TODO: SELECT COUNT(*) FROM members WHERE created_at >= ? AND created_at < ?.
        // Output: number of new members.
        return 0;
    }
}
