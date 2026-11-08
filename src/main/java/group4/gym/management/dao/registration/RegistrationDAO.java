package group4.gym.management.dao.registration;

import group4.gym.management.database.DBContext;
import group4.gym.management.exception.AppException;
import group4.gym.management.model.registration.DateAxis;
import group4.gym.management.model.registration.PaymentMethod;
import group4.gym.management.model.registration.Registration;
import group4.gym.management.model.registration.RegistrationFilter;
import group4.gym.management.model.registration.RegistrationStatus;
import group4.gym.management.model.registration.RegistrationView;
import group4.gym.management.util.AppClock;
import group4.gym.management.util.MemberKeyword;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Data access for registrations (table registrations, DR-04; owner TV3, payment columns written for TV4).
 * No delete, no update of PAID/CANCELLED rows (BR-02, BR-16). Every status change is conditional:
 * UPDATE ... WHERE id = ? AND status = 'PENDING' (0 rows = "no longer pending").
 * Every query uses PreparedStatement parameters (NFR-02). Methods without a Connection open their own with
 * DBContext.getConnection() and wrap SQLException in AppException; methods with a Connection run inside the
 * caller's transaction (SRS 9.1) and throw SQLException.
 */
public class RegistrationDAO {

    /**
     * Insert a PENDING registration with its snapshot (FR-22, inside the BR-06 transaction).
     */
    public int insert(Connection conn, Registration registration) throws SQLException {
        String sql = "INSERT INTO registrations (member_id, package_id, package_name_snapshot, duration_months_snapshot, price_snapshot, status, created_at, created_by) VALUES (?, ?, ?, ?, ?, 'PENDING', ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, registration.getMemberId());
            ps.setInt(2, registration.getPackageId());
            ps.setString(3, registration.getPackageNameSnapshot());
            ps.setInt(4, registration.getDurationMonthsSnapshot());
            ps.setInt(5, registration.getPriceSnapshot());
            ps.setObject(6, registration.getCreatedAt());
            ps.setInt(7, registration.getCreatedBy());
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (!keys.next()) {
                    throw new SQLException("INSERT INTO registrations returned no generated key");
                }
                return keys.getInt(1);
            }
        }
    }

    /**
     * Find a registration by ID.
     */
    public Registration findById(int id) {
        try (Connection conn = DBContext.getConnection()) {
            return findById(conn, id);
        } catch (SQLException ex) {
            throw new AppException("Cannot read registration " + id, ex);
        }
    }

    /**
     * Re-read a registration inside a transaction, after the member row is locked (FR-25, BR-06).
     */
    public Registration findById(Connection conn, int id) throws SQLException {
        String sql = "SELECT r.* FROM registrations r WHERE r.id = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? mapRow(rs) : null;
            }
        }
    }

    /**
     * Registration with member display data (FR-25 review step).
     */
    public RegistrationView findViewById(int id) {
        String sql = "SELECT r.*, m.full_name, m.phone, m.is_deleted FROM registrations r JOIN members m ON m.id = r.member_id WHERE r.id = ?";

        try (Connection conn = DBContext.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? mapView(rs) : null;
            }
        } catch (SQLException ex) {
            throw new AppException("Cannot read registration " + id, ex);
        }
    }

    /**
     * Every registration of a member, every status, newest first (shared interface SRS 9.1, used by TV2 for FR-13).
     */
    public List<Registration> findByMember(int memberId) {
        String sql = "SELECT r.* FROM registrations r WHERE r.member_id = ? ORDER BY r.created_at DESC, r.id DESC";

        try (Connection conn = DBContext.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, memberId);
            try (ResultSet rs = ps.executeQuery()) {
                List<Registration> list = new ArrayList<>();
                while (rs.next()) {
                    list.add(mapRow(rs));
                }
                return list;
            }
        } catch (SQLException ex) {
            throw new AppException("Cannot read registrations of member " + memberId, ex);
        }
    }

    /**
     * One page of the registration list (FR-24).
     * Axis CREATED_AT: created_at in [from, to+1); axis PAID_AT: paid_at in [from, to+1), which keeps PAID only (BR-13).
     *
     * @param fromInclusive DateRange.getFromInclusive()
     * @param toExclusive   DateRange.getToExclusive()
     */
    public List<RegistrationView> search(RegistrationFilter filter, LocalDateTime fromInclusive, LocalDateTime toExclusive,
            int offset, int limit) {
        String sql = "SELECT r.*, m.full_name, m.phone, m.is_deleted FROM registrations r JOIN members m ON m.id = r.member_id" + buildWhere(filter)
                + " ORDER BY " + axisColumn(filter) + " DESC, r.id DESC OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";
        try (Connection conn = DBContext.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            int nextParameter = bindFilters(ps, filter, fromInclusive, toExclusive);
            ps.setInt(nextParameter, offset);
            ps.setInt(nextParameter + 1, limit);
            try (ResultSet rs = ps.executeQuery()) {
                List<RegistrationView> list = new ArrayList<>();
                while (rs.next()) {
                    list.add(mapView(rs));
                }
                return list;
            }
        } catch (SQLException ex) {
            throw new AppException("Cannot search registrations", ex);
        }
    }

    /**
     * Total rows of search() for the same criteria (pagination).
     */
    public int count(RegistrationFilter filter, LocalDateTime fromInclusive, LocalDateTime toExclusive) {
        String sql = "SELECT COUNT(*) FROM registrations r JOIN members m ON m.id = r.member_id" + buildWhere(filter);
        try (Connection conn = DBContext.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            bindFilters(ps, filter, fromInclusive, toExclusive);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? rs.getInt(1) : 0;
            }
        } catch (SQLException ex) {
            throw new AppException("Cannot count registrations", ex);
        }
    }

    /**
     * Cancel one PENDING registration (FR-23, BR-02).
     */
    public boolean cancelIfPending(int id, int cancelledBy, String reason, LocalDateTime cancelledAt) {
        String sql = "UPDATE registrations SET status = 'CANCELLED', cancelled_at = ?, cancelled_by = ?, cancel_reason = ? WHERE id = ? AND status = 'PENDING'";
        try (Connection conn = DBContext.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setObject(1, cancelledAt);
            ps.setInt(2, cancelledBy);
            ps.setString(3, reason);
            ps.setInt(4, id);
            return ps.executeUpdate() == 1;
        } catch (SQLException ex) {
            throw new AppException("Cannot cancel registration " + id, ex);
        }
    }

    /**
     * Cancel every PENDING registration of a member (shared interface SRS 9.1, used by TV2 in the FR-15 transaction, BR-09).
     */
    public int cancelAllPending(Connection conn, int memberId, int byId, String reason) throws SQLException {
        String sql = "UPDATE registrations SET status = 'CANCELLED', cancelled_at = ?, cancelled_by = ?, cancel_reason = ? WHERE member_id = ? AND status = 'PENDING'";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setObject(1, AppClock.now());
            ps.setInt(2, byId);
            ps.setString(3, reason);
            ps.setInt(4, memberId);
            return ps.executeUpdate();
        }
    }

    /**
     * Whether the member still has a PAID registration with end_date >= today, active or upcoming
     * (shared interface SRS 9.1, used by TV2 in the FR-15 transaction, BR-09).
     */
    public boolean hasUnexpiredPaid(Connection conn, int memberId, LocalDate today) throws SQLException {
        String sql = "SELECT COUNT(*) FROM registrations WHERE member_id = ? AND status = 'PAID' AND end_date >= ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, memberId);
            ps.setObject(2, today);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }
        }
    }

    /**
     * Largest end_date of the member's PAID registrations (BR-05), read inside the FR-25 transaction.
     */
    public LocalDate findMaxPaidEndDate(Connection conn, int memberId) throws SQLException {
        String sql = "SELECT MAX(end_date) FROM registrations WHERE member_id = ? AND status = 'PAID'";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, memberId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? rs.getObject(1, LocalDate.class) : null;
            }
        }
    }

    /**
     * Same as above, outside a transaction (FR-25 review step only).
     */
    public LocalDate findMaxPaidEndDate(int memberId) {
        try (Connection conn = DBContext.getConnection()) {
            return findMaxPaidEndDate(conn, memberId);
        } catch (SQLException ex) {
            throw new AppException("Cannot read the last end date of member " + memberId, ex);
        }
    }

    /**
     * PENDING -> PAID with the schedule, in one statement (FR-25, BR-02, used by PaymentService of TV4).
     */
    public boolean markPaidIfPending(Connection conn, int id, PaymentMethod method, LocalDateTime paidAt, int confirmedBy,
            LocalDate startDate, LocalDate endDate) throws SQLException {
        String sql = "UPDATE registrations SET status = 'PAID', payment_method = ?, paid_at = ?, confirmed_by = ?, start_date = ?, end_date = ? WHERE id = ? AND status = 'PENDING'";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, method.name());
            ps.setObject(2, paidAt);
            ps.setInt(3, confirmedBy);
            ps.setObject(4, startDate);
            ps.setObject(5, endDate);
            ps.setInt(6, id);
            return ps.executeUpdate() == 1;
        }
    }

    /**
     * Column of the date-range axis, from a fixed switch on DateAxis, never from user text (NFR-02). Default is CREATED_AT.
     */
    private String axisColumn(RegistrationFilter filter) {
        return filter.getAxis() == DateAxis.PAID_AT ? "r.paid_at" : "r.created_at";
    }

    /**
     * Classified member keyword of the filter (FR-24); null when not filtered.
     */
    private MemberKeyword keywordOf(RegistrationFilter filter) {
        return MemberKeyword.parse(filter.getMemberKeyword());
    }

    /**
     * Bind the keyword placeholder written by buildWhere() (none for NONE) and return the next parameter index.
     */
    private int bindKeyword(PreparedStatement ps, int i, MemberKeyword keyword) throws SQLException {
        if (keyword == null) {
            return i;
        }
        switch (keyword.getType()) {
            case PHONE:
                ps.setString(i++, keyword.getValue());
                break;
            case NAME:
                ps.setString(i++, keyword.getLikePattern());
                break;
            default:
                break;
        }
        return i;
    }

    /**
     * WHERE clause shared by search() and count(). Placeholders, in order: status, package, member keyword
     * (one for PHONE or NAME, none for NONE), range start, range end; bindFilters() sets them in the same order.
     * The keyword is classified by MemberKeyword with the same rules as the member list (FR-11, FR-24).
     */
    private String buildWhere(RegistrationFilter filter) {
        StringBuilder where = new StringBuilder(" WHERE 1 = 1");
        if (filter.getStatus() != null) {
            where.append(" AND r.status = ?");
        }
        if (filter.getPackageId() != null) {
            where.append(" AND r.package_id = ?");
        }
        MemberKeyword keyword = keywordOf(filter);
        if (keyword != null) {
            switch (keyword.getType()) {
                case PHONE:
                    where.append(" AND m.phone = ?");
                    break;
                case NAME:
                    where.append(" AND m.full_name COLLATE Latin1_General_100_CI_AS LIKE ? ESCAPE '\\'");
                    break;
                default:
                    // NONE ("5", "HV000123", a partial phone...): matches nothing (AC-09).
                    where.append(" AND 1 = 0");
                    break;
            }
        }
        String axis = axisColumn(filter);
        where.append(" AND ").append(axis).append(" >= ? AND ").append(axis).append(" < ?");
        return where.toString();
    }

    /**
     * Binds the shared search filters and returns the next free parameter index.
     */
    private int bindFilters(PreparedStatement ps, RegistrationFilter filter,
            LocalDateTime fromInclusive, LocalDateTime toExclusive) throws SQLException {
        int index = 1;
        if (filter.getStatus() != null) {
            ps.setString(index++, filter.getStatus().name());
        }
        if (filter.getPackageId() != null) {
            ps.setInt(index++, filter.getPackageId());
        }
        index = bindKeyword(ps, index, keywordOf(filter));
        ps.setObject(index++, fromInclusive);
        ps.setObject(index++, toExclusive);
        return index;
    }

    private Registration mapRow(ResultSet rs) throws SQLException {
        Registration r = new Registration();
        r.setId(rs.getInt("id"));
        r.setMemberId(rs.getInt("member_id"));
        r.setPackageId(rs.getInt("package_id"));
        r.setPackageNameSnapshot(rs.getString("package_name_snapshot"));
        r.setDurationMonthsSnapshot(rs.getInt("duration_months_snapshot"));
        r.setPriceSnapshot(rs.getInt("price_snapshot"));
        r.setStatus(RegistrationStatus.valueOf(rs.getString("status")));
        r.setCreatedAt(rs.getObject("created_at", LocalDateTime.class));
        r.setCreatedBy(rs.getInt("created_by"));
        String method = rs.getString("payment_method");
        r.setPaymentMethod(method == null ? null : PaymentMethod.valueOf(method));
        r.setPaidAt(rs.getObject("paid_at", LocalDateTime.class));
        r.setConfirmedBy(getNullableInt(rs, "confirmed_by"));
        r.setStartDate(rs.getObject("start_date", LocalDate.class));
        r.setEndDate(rs.getObject("end_date", LocalDate.class));
        r.setCancelledAt(rs.getObject("cancelled_at", LocalDateTime.class));
        r.setCancelledBy(getNullableInt(rs, "cancelled_by"));
        r.setCancelReason(rs.getString("cancel_reason"));
        return r;
    }

    private RegistrationView mapView(ResultSet rs) throws SQLException {
        Registration registration = mapRow(rs);
        RegistrationView view = new RegistrationView();
        view.setRegistration(registration);
        view.setMemberCode(String.format("HV%06d", registration.getMemberId()));
        view.setMemberName(rs.getString("full_name"));
        view.setMemberPhone(rs.getString("phone"));
        view.setMemberDeleted(rs.getBoolean("is_deleted"));
        return view;
    }

    private Integer getNullableInt(ResultSet rs, String column) throws SQLException {
        int value = rs.getInt(column);
        return rs.wasNull() ? null : value;
    }
}
