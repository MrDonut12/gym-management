package group4.gym.management.dao.member;

import group4.gym.management.database.DBContext;
import group4.gym.management.exception.AppException;
import group4.gym.management.exception.ValidationException;
import group4.gym.management.model.member.*;
import java.sql.*;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Data access for members (table members, DL-02). No physical deletion (QT-16).
 * Every query uses PreparedStatement parameters (CL-02). Methods without a
 * Connection open their own with DBContext.getConnection() and wrap
 * SQLException in AppException; methods with a Connection run inside the
 * caller's transaction (SRS 9.1) and throw SQLException.
 *
 * Search keyword (CN-11): "HV000123" or "123" -> id exact; "0912345678" ->
 * phone exact; otherwise full_name LIKE %keyword% (case-insensitive). A partial
 * phone therefore finds nothing (KT-09).
 *
 * @author toan
 */
public class MemberDAO {

    /**
     * Find a member by ID, including soft-deleted (shared interface SRS 9.1,
     * used by TV3).
     */
    public Member findById(int id) {
        // Query: SELECT ... FROM members WHERE id = ?.
        // Output: Member; null if it does not exist.
        String sql = "SELECT * FROM dbo.members WHERE id = ?";
        try (Connection conn = DBContext.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? mapRow(rs) : null;
            }
        } catch (SQLException ex) {
            throw new AppException("Cannot read member.", ex);
        }
    }

    /**
     * Lock the member row at the start of a QT-06 transaction (shared interface
     * SRS 9.1, used by CN-15, CN-22, CN-25).
     */
    public Member lockForUpdate(Connection conn, int memberId) throws SQLException {
        // Query: SELECT ... FROM members WITH (UPDLOCK, HOLDLOCK) WHERE id = ?.
        // Output: the locked Member; null if it does not exist.
        String sql = "SELECT * FROM dbo.members WITH (UPDLOCK, HOLDLOCK) WHERE id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, memberId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? mapRow(rs) : null;
            }
        }
    }

    /**
     * Find a member by phone, including soft-deleted (CN-12: a duplicate phone
     * shows the code of the profile using it).
     */
    public Member findByPhone(String phone) {
        // Query: SELECT ... FROM members WHERE phone = ?.
        // Output: Member; null if it does not exist.
        String sql = "SELECT * FROM dbo.members WHERE phone = ?";
        try (Connection conn = DBContext.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, phone);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? mapRow(rs) : null;
            }
        } catch (SQLException ex) {
            throw new AppException("Cannot look up member phone.", ex);
        }
    }

    /**
     * One page of the member list (CN-11), newest first, with the PAID
     * aggregates needed by QT-07.
     *
     * @param keyword null = no search
     * @param today AppClock.today(), used for paidCoveringToday
     */
    public List<MemberListItem> search(String keyword, MemberScope scope, LocalDate today, int offset, int limit) {
        // Query: Read PAID aggregates using subqueries and paginate with OFFSET/FETCH.
        // Output: list of MemberListItem.
        checkPage(offset, limit);
        Filter filter = filter(keyword, scope);
        String sql = """
                SELECT m.*,
                    (SELECT MAX(r.end_date) FROM dbo.registrations r
                     WHERE r.member_id = m.id AND r.status = 'PAID') AS current_end_date,
                    CASE WHEN EXISTS (
                        SELECT 1 FROM dbo.registrations r
                        WHERE r.member_id = m.id AND r.status = 'PAID'
                          AND r.start_date <= ? AND r.end_date >= ?
                    ) THEN 1 ELSE 0 END AS covers_today
                FROM dbo.members m
                """ + filter.where() + " ORDER BY m.created_at DESC, m.id DESC "
                + "OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";
        try (Connection conn = DBContext.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setObject(1, today);
            ps.setObject(2, today);
            int i = bindFilter(ps, filter, 3);
            ps.setInt(i++, offset);
            ps.setInt(i, limit);
            try (ResultSet rs = ps.executeQuery()) {
                List<MemberListItem> result = new ArrayList<>();
                while (rs.next()) {
                    MemberListItem item = new MemberListItem();
                    item.setMember(mapRow(rs));
                    Date lastEnd = rs.getDate("current_end_date");
                    item.setCurrentEndDate(lastEnd == null ? null : lastEnd.toLocalDate());
                    item.setPaidCoveringToday(rs.getInt("covers_today") == 1);
                    result.add(item);
                }
                return result;
            }
        } catch (SQLException ex) {
            throw new AppException("Cannot search members.", ex);
        }
    }

    /**
     * Total rows of search() for the same keyword and scope (pagination).
     */
    public int count(String keyword, MemberScope scope) {
        // Query: SELECT COUNT(*) FROM members WHERE <scope> AND <keyword>.
        // Output: number of matching members.
        Filter filter = filter(keyword, scope);
        String sql = "SELECT COUNT(*) FROM dbo.members m" + filter.where();
        try (Connection conn = DBContext.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            bindFilter(ps, filter, 1);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? rs.getInt(1) : 0;
            }
        } catch (SQLException ex) {
            throw new AppException("Cannot count members.", ex);
        }
    }

    /**
     * Search members that are not soft-deleted (shared interface SRS 9.1: TV3
     * picks a member for CN-22).
     */
    public List<Member> searchActive(String keyword, int limit) {
        // Query: Same keyword rules as search(), AND is_deleted = 0, ORDER BY full_name, TOP (?).
        // Output: list of Member.
        if (limit <= 0 || limit > 100) {
            throw new IllegalArgumentException("limit must be between 1 and 100");
        }
        Filter filter = filter(keyword, MemberScope.ACTIVE);
        String sql = "SELECT TOP (?) m.* FROM dbo.members m" + filter.where()
                + " ORDER BY m.full_name, m.id";
        try (Connection conn = DBContext.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, limit);
            bindFilter(ps, filter, 2);
            try (ResultSet rs = ps.executeQuery()) {
                List<Member> result = new ArrayList<>();
                while (rs.next()) {
                    result.add(mapRow(rs));
                }
                return result;
            }
        } catch (SQLException ex) {
            throw new AppException("Cannot search active members.", ex);
        }
    }

    /**
     * Members expiring soon (QT-08, CN-17): not soft-deleted, some PAID covers
     * today, and the largest PAID end_date is within [today, today +
     * maxDaysAhead]. A member with a follow-on PAID is excluded because
     * MAX(end_date) moves forward.
     */
    public List<ExpiringMember> findExpiringSoon(LocalDate today, int maxDaysAhead) {
        // Query: Find the latest PAID end_date per member; exclude deleted and already-renewed profiles.
        // Output: list of ExpiringMember.
        if (maxDaysAhead < 0 || maxDaysAhead > 366) {
            throw new IllegalArgumentException("Invalid expiration window");
        }
        String sql = """
                SELECT m.*, latest.end_date AS current_end_date,
                       latest.package_name_snapshot
                FROM dbo.members m
                CROSS APPLY (
                    SELECT TOP (1) r.end_date, r.package_name_snapshot
                    FROM dbo.registrations r
                    WHERE r.member_id = m.id AND r.status = 'PAID'
                    ORDER BY r.end_date DESC, r.id DESC
                ) latest
                WHERE m.is_deleted = 0
                  AND latest.end_date BETWEEN ? AND ?
                  AND EXISTS (
                      SELECT 1 FROM dbo.registrations r
                      WHERE r.member_id = m.id AND r.status = 'PAID'
                        AND r.start_date <= ? AND r.end_date >= ?
                  )
                ORDER BY latest.end_date ASC, m.id ASC
                """;
        try (Connection conn = DBContext.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setObject(1, today);
            ps.setObject(2, today.plusDays(maxDaysAhead));
            ps.setObject(3, today);
            ps.setObject(4, today);
            try (ResultSet rs = ps.executeQuery()) {
                List<ExpiringMember> result = new ArrayList<>();
                while (rs.next()) {
                    ExpiringMember item = new ExpiringMember();
                    item.setMember(mapRow(rs));
                    item.setPackageName(rs.getString("package_name_snapshot"));
                    LocalDate end = rs.getDate("current_end_date").toLocalDate();
                    item.setEndDate(end);
                    item.setDaysLeft((int) ChronoUnit.DAYS.between(today, end) + 1);
                    result.add(item);
                }
                return result;
            }
        } catch (SQLException ex) {
            throw new AppException("Cannot read expiring members.", ex);
        }
    }

    /**
     * Insert a member profile (CN-12).
     */
    public int insert(Member member) {
        // Query: INSERT (full_name, phone, email, date_of_birth, gender (label), is_deleted = 0, created_at, created_by).
        // Output: the generated ID.
        String sql = """
                INSERT INTO dbo.members
                (full_name, phone, email, date_of_birth, gender, is_deleted, created_at, created_by)
                VALUES (?, ?, ?, ?, ?, 0, ?, ?)
                """;
        try (Connection conn = DBContext.getConnection(); PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, member.getFullName());
            ps.setString(2, member.getPhone());
            ps.setString(3, member.getEmail());
            ps.setObject(4, member.getDateOfBirth());
            ps.setString(5, member.getGender() == null ? null : member.getGender().getLabel());
            ps.setObject(6, member.getCreatedAt());
            ps.setObject(7, member.getCreatedBy());
            if (ps.executeUpdate() != 1) {
                throw new SQLException("Member was not inserted");
            }
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    return keys.getInt(1);
                }
                throw new SQLException("Missing generated member ID");
            }
        } catch (SQLException ex) {
            throw saveFailure(ex, member);
        }
    }

    /**
     * Update a profile that is not soft-deleted (CN-14). id and created_at are
     * never updated.
     */
    public boolean update(Member member) {
        // Query: UPDATE members SET full_name, phone, email, date_of_birth, gender WHERE id = ? AND is_deleted = 0.
        // Output: true if exactly 1 row was updated; false if missing or soft-deleted.
        if (member == null || member.getId() == null) {
            return false;
        }
        String sql = """
                UPDATE dbo.members
                SET full_name = ?, phone = ?, email = ?, date_of_birth = ?, gender = ?
                WHERE id = ? AND is_deleted = 0
                """;
        try (Connection conn = DBContext.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, member.getFullName());
            ps.setString(2, member.getPhone());
            ps.setString(3, member.getEmail());
            ps.setObject(4, member.getDateOfBirth());
            ps.setString(5, member.getGender() == null ? null : member.getGender().getLabel());
            ps.setInt(6, member.getId());
            return ps.executeUpdate() == 1;
        } catch (SQLException ex) {
            throw saveFailure(ex, member);
        }
    }

    /**
     * Soft-delete inside the QT-06/QT-09 transaction of CN-15.
     */
    public boolean markDeleted(Connection conn, int id) throws SQLException {
        // Query: UPDATE members SET is_deleted = 1 WHERE id = ? AND is_deleted = 0.
        // Output: true if exactly 1 row was updated.
        String sql = "UPDATE dbo.members SET is_deleted = 1 WHERE id = ? AND is_deleted = 0";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() == 1;
        }
    }

    /**
     * Restore a soft-deleted profile (CN-16, QT-10); code, created_at and
     * history are kept.
     */
    public boolean restore(int id) {
        // Query: UPDATE members SET is_deleted = 0 WHERE id = ? AND is_deleted = 1.
        // Output: true if exactly 1 row was updated.
        String sql = "UPDATE dbo.members SET is_deleted = 0 WHERE id = ? AND is_deleted = 1";
        try (Connection conn = DBContext.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() == 1;
        } catch (SQLException ex) {
            throw new AppException("Cannot restore member.", ex);
        }
    }

    /**
     * Read the database's Vietnamese gender label using the existing Gender
     * enum.
     */
    private Member mapRow(ResultSet rs) throws SQLException {
        Member member = new Member();
        member.setId(rs.getInt("id"));
        member.setFullName(rs.getString("full_name"));
        member.setPhone(rs.getString("phone"));
        member.setEmail(rs.getString("email"));
        Date birthday = rs.getDate("date_of_birth");
        member.setDateOfBirth(birthday == null ? null : birthday.toLocalDate());
        member.setGender(Gender.fromLabel(rs.getString("gender")));
        member.setDeleted(rs.getBoolean("is_deleted"));
        member.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        int creator = rs.getInt("created_by");
        member.setCreatedBy(rs.wasNull() ? null : creator);
        return member;
    }

    /**
     * All SQL fragments here are constants; user content only enters bound
     * parameters.
     */
    private record Filter(String where, Object value) {

    }

    private Filter filter(String keyword, MemberScope scope) {
        MemberScope safeScope = scope == null ? MemberScope.ACTIVE : scope;
        String where = switch (safeScope) {
            case ACTIVE ->
                " WHERE m.is_deleted = 0";
            case DELETED ->
                " WHERE m.is_deleted = 1";
            case ALL ->
                " WHERE 1 = 1";
        };
        String q = keyword == null ? "" : keyword.trim();
        if (q.length() > 100) {
            throw new IllegalArgumentException("Search keyword too long");
        }
        if (q.isEmpty()) {
            return new Filter(where, null);
        }

        if (q.matches("(?i)HV[0-9]{6}")) {
            return new Filter(where + " AND m.id = ?", idOrMinusOne(q.substring(2)));
        }
        if (q.matches("[0-9]+")) {
            // Numbers starting with zero are phone searches. A partial phone cannot match an ID.
            return q.startsWith("0")
                    ? new Filter(where + " AND m.phone = ?", q)
                    : new Filter(where + " AND m.id = ?", idOrMinusOne(q));
        }
        return new Filter(where + " AND m.full_name COLLATE Vietnamese_100_CI_AS LIKE ? ESCAPE '!'",
                "%" + q.replace("!", "!!").replace("%", "!%")
                        .replace("_", "!_").replace("[", "![") + "%");
    }

    private static int idOrMinusOne(String raw) {
        try {
            return Integer.parseInt(raw);
        } catch (NumberFormatException ex) {
            return -1; // Never match a valid ID; malformed/overflowing search is not a server error.
        }
    }

    private static int bindFilter(PreparedStatement ps, Filter filter, int index) throws SQLException {
        if (filter.value() != null) {
            ps.setObject(index++, filter.value());
        }
        return index;
    }

    private static void checkPage(int offset, int limit) {
        if (offset < 0 || limit <= 0 || limit > 100) {
            throw new IllegalArgumentException("Invalid pagination parameters");
        }
    }

    /**
     * Database UNIQUE is the last line of defense against concurrent duplicate
     * phones.
     */
    private RuntimeException saveFailure(SQLException ex, Member member) {
        for (SQLException cause = ex; cause != null; cause = cause.getNextException()) {
            if (cause.getErrorCode() == 2601 || cause.getErrorCode() == 2627) {
                Member existing = member.getPhone() == null ? null : findByPhone(member.getPhone());
                String owner = existing == null ? "another profile" : existing.getCode();
                return new ValidationException("Duplicate phone number.",
                        Map.of("phone",
                                "This phone number is already associated with member profile " + owner + "."));
            }
        }
        return new AppException("Cannot save member.", ex);
    }
}
