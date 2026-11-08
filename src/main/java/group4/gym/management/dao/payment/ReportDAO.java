package group4.gym.management.dao.payment;

import group4.gym.management.database.DBContext;
import group4.gym.management.exception.AppException;
import group4.gym.management.model.payment.RevenueGranularity;
import group4.gym.management.model.payment.RevenueRow;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Aggregate queries for the reports (FR-26 to FR-28). Every range is [fromInclusive, toExclusive) (BR-13).
 * Revenue = SUM(price_snapshot) of PAID registrations with paid_at in the range; PENDING/CANCELLED excluded (BR-12).
 * Every query uses PreparedStatement parameters (NFR-02). Methods open their own connection with
 * DBContext.getConnection() and wrap SQLException in AppException.
 */
public class ReportDAO {

    private static final String DAY_PERIOD = "CAST(paid_at AS DATE)";

    private static final String MONTH_PERIOD = "DATEFROMPARTS(YEAR(paid_at), MONTH(paid_at), 1)";

    /**
     * Revenue per day or month (FR-26). Periods without revenue are omitted.
     */
    public List<RevenueRow> revenueByPeriod(LocalDateTime fromInclusive, LocalDateTime toExclusive, RevenueGranularity granularity) {
        requireRange(fromInclusive, toExclusive);
        // The period expression comes from a fixed switch, never from user input (NFR-02).
        String period = switch (granularity == null ? RevenueGranularity.DAY : granularity) {
            case DAY -> DAY_PERIOD;
            case MONTH -> MONTH_PERIOD;
        };
        String sql = "SELECT " + period + " AS period_start, COUNT(*) AS transaction_count,"
                + " SUM(CAST(price_snapshot AS BIGINT)) AS revenue"
                + " FROM dbo.registrations"
                + " WHERE status = 'PAID' AND paid_at >= ? AND paid_at < ?"
                + " GROUP BY " + period
                + " ORDER BY period_start";

        List<RevenueRow> rows = new ArrayList<>();
        try (Connection conn = DBContext.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setTimestamp(1, Timestamp.valueOf(fromInclusive));
            ps.setTimestamp(2, Timestamp.valueOf(toExclusive));
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    RevenueRow row = new RevenueRow();
                    row.setPeriodStart(rs.getDate("period_start").toLocalDate());
                    row.setTransactionCount(rs.getInt("transaction_count"));
                    row.setRevenue(rs.getLong("revenue"));
                    rows.add(row);
                }
            }
            return rows;
        } catch (SQLException ex) {
            throw new AppException("Cannot load revenue by period", ex);
        }
    }

    /**
     * Revenue per package (FR-27): grouped by package_id, labelled with the CURRENT package name (BR-12).
     */
    public List<RevenueRow> revenueByPackage(LocalDateTime fromInclusive, LocalDateTime toExclusive) {
        requireRange(fromInclusive, toExclusive);
        String sql = "SELECT r.package_id, p.name, COUNT(*) AS transaction_count,"
                + " SUM(CAST(r.price_snapshot AS BIGINT)) AS revenue"
                + " FROM dbo.registrations r"
                + " JOIN dbo.packages p ON p.id = r.package_id"
                + " WHERE r.status = 'PAID' AND r.paid_at >= ? AND r.paid_at < ?"
                + " GROUP BY r.package_id, p.name"
                + " ORDER BY revenue DESC, r.package_id";

        List<RevenueRow> rows = new ArrayList<>();
        try (Connection conn = DBContext.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setTimestamp(1, Timestamp.valueOf(fromInclusive));
            ps.setTimestamp(2, Timestamp.valueOf(toExclusive));
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    RevenueRow row = new RevenueRow();
                    row.setPackageId(rs.getInt("package_id"));
                    row.setLabel(rs.getNString("name"));
                    row.setTransactionCount(rs.getInt("transaction_count"));
                    row.setRevenue(rs.getLong("revenue"));
                    rows.add(row);
                }
            }
            return rows;
        } catch (SQLException ex) {
            throw new AppException("Cannot load revenue by package", ex);
        }
    }

    /**
     * Count profiles by soft-delete flag (FR-28).
     */
    public int countMembers(boolean deleted) {
        String sql = "SELECT COUNT(*) FROM dbo.members WHERE is_deleted = ?";
        try (Connection conn = DBContext.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setBoolean(1, deleted);
            return singleInt(ps);
        } catch (SQLException ex) {
            throw new AppException("Cannot count members", ex);
        }
    }

    /**
     * Members with a PAID registration covering today, i.e. membership status ACTIVE (FR-28, BR-07).
     * Counts every profile, as FR-28 does not exclude soft-deleted ones; a soft-deleted profile
     * cannot hold an unexpired PAID anyway (BR-09).
     */
    public int countMembersWithActivePackage(LocalDate today) {
        if (today == null) {
            throw new IllegalArgumentException("today is required");
        }
        String sql = "SELECT COUNT(DISTINCT member_id) FROM dbo.registrations"
                + " WHERE status = 'PAID' AND start_date <= ? AND end_date >= ?";
        try (Connection conn = DBContext.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDate(1, Date.valueOf(today));
            ps.setDate(2, Date.valueOf(today));
            return singleInt(ps);
        } catch (SQLException ex) {
            throw new AppException("Cannot count members with an active package", ex);
        }
    }

    /**
     * New members by created_at in the range, including profiles soft-deleted later (FR-28).
     */
    public int countNewMembers(LocalDateTime fromInclusive, LocalDateTime toExclusive) {
        requireRange(fromInclusive, toExclusive);
        String sql = "SELECT COUNT(*) FROM dbo.members WHERE created_at >= ? AND created_at < ?";
        try (Connection conn = DBContext.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setTimestamp(1, Timestamp.valueOf(fromInclusive));
            ps.setTimestamp(2, Timestamp.valueOf(toExclusive));
            return singleInt(ps);
        } catch (SQLException ex) {
            throw new AppException("Cannot count new members", ex);
        }
    }

    /**
     * Rejects a missing bound or an empty range before any SQL runs.
     */
    private void requireRange(LocalDateTime fromInclusive, LocalDateTime toExclusive) {
        if (fromInclusive == null || toExclusive == null || !fromInclusive.isBefore(toExclusive)) {
            throw new IllegalArgumentException("Invalid date range");
        }
    }

    /**
     * Runs a COUNT query and returns its single value.
     */
    private int singleInt(PreparedStatement ps) throws SQLException {
        try (ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1);
            }
            throw new SQLException("Count query returned no row");
        }
    }
}
