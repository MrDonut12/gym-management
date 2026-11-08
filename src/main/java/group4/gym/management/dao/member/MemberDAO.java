package group4.gym.management.dao.member;

import group4.gym.management.database.DBContext;
import group4.gym.management.exception.AppException;
import group4.gym.management.exception.ValidationException;
import group4.gym.management.model.member.ExpiringMember;
import group4.gym.management.model.member.Gender;
import group4.gym.management.model.member.Member;
import group4.gym.management.model.member.MemberListItem;
import group4.gym.management.model.member.MemberScope;
import group4.gym.management.util.MemberKeyword;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.sql.Types;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Data access for members (table members, DR-02). No physical deletion (BR-16).
 * Every query uses PreparedStatement parameters (NFR-02). Methods without a Connection open their own with
 * DBContext.getConnection(); methods with a Connection run inside the caller's transaction (BR-06).
 */
public class MemberDAO {

    /**
     * Finds a member by ID, including soft-deleted profiles.
     * Returns null if the member does not exist.
     */
    public Member findById(int id) {
        String sql = "SELECT * FROM dbo.members WHERE id = ?";

        try (Connection conn = DBContext.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs);
                }
                return null;
            }
        } catch (SQLException ex) {
            throw new AppException("Cannot read member " + id, ex);
        }
    }

    /**
     * Locks and returns a member during a transaction to prevent conflicting updates.
     * Returns null if the member does not exist.
     */
    public Member lockForUpdate(Connection conn, int memberId) throws SQLException {
        String sql = "SELECT * FROM dbo.members WITH (UPDLOCK, HOLDLOCK) WHERE id = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, memberId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs);
                }
                return null;
            }
        }
    }

    /**
     * Finds a member by exact phone number, including soft-deleted profiles.
     * Returns null if no matching profile exists.
     */
    public Member findByPhone(String phone) {
        phone = getKeyword(phone);
        if (phone.isEmpty()) {
            return null;
        }

        String sql = "SELECT * FROM dbo.members WHERE phone = ?";

        try (Connection conn = DBContext.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, phone);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs);
                }
                return null;
            }
        } catch (SQLException ex) {
            throw new AppException("Cannot find member by phone", ex);
        }
    }

    /**
     * Returns a page of members matching the search box, newest first (FR-11).
     * The keyword is classified by MemberKeyword: HV + 6 digits -> exact code, a valid phone -> exact phone,
     * no digit -> partial name; anything else matches nothing (AC-09). A blank keyword lists every member of the scope.
     * Applies the deletion filter and includes paid-package expiry and coverage information.
     */
    public List<MemberListItem> search(String keyword, MemberScope scope,
            LocalDate today, int offset, int limit) {
        if (today == null || offset < 0 || limit <= 0) {
            throw new IllegalArgumentException("Invalid date or pagination");
        }
        MemberKeyword parsed = MemberKeyword.parse(keyword);

        String sql = "SELECT m.*, "
                + "(SELECT MAX(r.end_date) FROM dbo.registrations r "
                + " WHERE r.member_id = m.id AND r.status = 'PAID') AS current_end_date, "
                + "CASE WHEN EXISTS (SELECT 1 FROM dbo.registrations r "
                + " WHERE r.member_id = m.id AND r.status = 'PAID' "
                + " AND r.start_date <= ? AND r.end_date >= ?) "
                + "THEN 1 ELSE 0 END AS covers_today "
                + "FROM dbo.members m WHERE " + getScopeCondition(scope)
                + getKeywordCondition(parsed)
                + " ORDER BY m.created_at DESC, m.id DESC"
                + " OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";

        List<MemberListItem> list = new ArrayList<>();

        try (Connection conn = DBContext.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDate(1, Date.valueOf(today));
            ps.setDate(2, Date.valueOf(today));
            int index = bindKeyword(ps, 3, parsed);
            ps.setInt(index++, offset);
            ps.setInt(index, limit);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    MemberListItem item = new MemberListItem();
                    item.setMember(mapRow(rs));
                    item.setPaidCoveringToday(rs.getBoolean("covers_today"));

                    Date endDate = rs.getDate("current_end_date");
                    if (endDate != null) {
                        item.setCurrentEndDate(endDate.toLocalDate());
                    }

                    list.add(item);
                }
            }
            return list;
        } catch (SQLException ex) {
            throw new AppException("Cannot search members", ex);
        }
    }

    /**
     * Counts members matching the keyword and deletion filter for pagination.
     */
    public int count(String keyword, MemberScope scope) {
        MemberKeyword parsed = MemberKeyword.parse(keyword);
        String sql = "SELECT COUNT(*) FROM dbo.members m WHERE " + getScopeCondition(scope)
                + getKeywordCondition(parsed);

        try (Connection conn = DBContext.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            bindKeyword(ps, 1, parsed);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
                throw new SQLException("Member count returned no row");
            }
        } catch (SQLException ex) {
            throw new AppException("Cannot count members", ex);
        }
    }

    /**
     * Finds up to the requested limit of non-deleted members (FR-22 member picker), with the same keyword
     * rules as the member list (code, phone or partial name; see MemberKeyword).
     */
    public List<Member> searchActive(String keyword, int limit) {
        if (limit <= 0) {
            throw new IllegalArgumentException("limit must be > 0");
        }
        MemberKeyword parsed = MemberKeyword.parse(keyword);

        String sql = "SELECT TOP (?) m.* FROM dbo.members m WHERE "
                + getScopeCondition(MemberScope.ACTIVE)
                + getKeywordCondition(parsed)
                + " ORDER BY m.full_name, m.id";

        List<Member> list = new ArrayList<>();

        try (Connection conn = DBContext.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, limit);
            bindKeyword(ps, 2, parsed);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapRow(rs));
                }
            }
            return list;
        } catch (SQLException ex) {
            throw new AppException("Cannot search active member profiles", ex);
        }
    }

    /**
     * Lists non-deleted members with a paid package valid today whose latest paid package
     * ends between today and today plus maxDaysAhead, including both dates.
     */
    public List<ExpiringMember> findExpiringSoon(LocalDate today, int maxDaysAhead) {
        if (today == null || maxDaysAhead < 0) {
            throw new IllegalArgumentException("Invalid date or maxDaysAhead");
        }

        String sql = "SELECT m.*, r.package_name_snapshot, r.end_date AS current_end_date, "
                + "DATEDIFF(DAY, ?, r.end_date) + 1 AS days_left "
                + "FROM dbo.members m JOIN dbo.registrations r ON r.member_id = m.id "
                + "WHERE m.is_deleted = 0 AND r.status = 'PAID' "
                + "AND r.end_date BETWEEN ? AND ? "
                + "AND r.id = (SELECT TOP (1) r2.id FROM dbo.registrations r2 "
                + " WHERE r2.member_id = m.id AND r2.status = 'PAID' "
                + " ORDER BY r2.end_date DESC, r2.id DESC) "
                + "AND EXISTS (SELECT 1 FROM dbo.registrations r3 "
                + " WHERE r3.member_id = m.id AND r3.status = 'PAID' "
                + " AND r3.start_date <= ? AND r3.end_date >= ?) "
                + "ORDER BY r.end_date ASC, m.id ASC";

        List<ExpiringMember> list = new ArrayList<>();

        try (Connection conn = DBContext.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDate(1, Date.valueOf(today));
            ps.setDate(2, Date.valueOf(today));
            ps.setDate(3, Date.valueOf(today.plusDays(maxDaysAhead)));
            ps.setDate(4, Date.valueOf(today));
            ps.setDate(5, Date.valueOf(today));

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    ExpiringMember item = new ExpiringMember();
                    item.setMember(mapRow(rs));
                    item.setPackageName(rs.getString("package_name_snapshot"));
                    item.setEndDate(rs.getDate("current_end_date").toLocalDate());
                    item.setDaysLeft(rs.getInt("days_left"));
                    list.add(item);
                }
            }
            return list;
        } catch (SQLException ex) {
            throw new AppException("Cannot read expiring members", ex);
        }
    }

    /**
     * Saves a new member profile and returns the database-generated ID.
     */
    public int insert(Member member) {
        if (member == null) {
            throw new IllegalArgumentException("member is required");
        }

        String sql = "INSERT INTO dbo.members "
                + "(full_name, phone, email, date_of_birth, gender, is_deleted, created_at, created_by) "
                + "VALUES (?, ?, ?, ?, ?, 0, ?, ?)";

        try (Connection conn = DBContext.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setNString(1, member.getFullName());
            ps.setString(2, member.getPhone());
            ps.setString(3, member.getEmail());

            if (member.getDateOfBirth() == null) {
                ps.setNull(4, Types.DATE);
            } else {
                ps.setDate(4, Date.valueOf(member.getDateOfBirth()));
            }

            if (member.getGender() == null) {
                ps.setNull(5, Types.NVARCHAR);
            } else {
                ps.setNString(5, member.getGender().getLabel());
            }

            if (member.getCreatedAt() == null) {
                ps.setNull(6, Types.TIMESTAMP);
            } else {
                ps.setTimestamp(6, Timestamp.valueOf(member.getCreatedAt()));
            }

            if (member.getCreatedBy() == null) {
                ps.setNull(7, Types.INTEGER);
            } else {
                ps.setInt(7, member.getCreatedBy());
            }

            if (ps.executeUpdate() != 1) {
                throw new SQLException("Member insert did not affect exactly one row");
            }
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
                throw new SQLException("Member insert returned no generated key");
            }
        } catch (SQLException ex) {
            throw saveFailure(ex, member);
        }
    }

    /**
     * Updates the editable details of a non-deleted member.
     * Returns true when one profile is updated.
     */
    public boolean update(Member member) {
        if (member == null || member.getId() == null) {
            throw new IllegalArgumentException("member.id is required for update");
        }

        String sql = "UPDATE dbo.members "
                + "SET full_name = ?, phone = ?, email = ?, date_of_birth = ?, gender = ? "
                + "WHERE id = ? AND is_deleted = 0";

        try (Connection conn = DBContext.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setNString(1, member.getFullName());
            ps.setString(2, member.getPhone());
            ps.setString(3, member.getEmail());

            if (member.getDateOfBirth() == null) {
                ps.setNull(4, Types.DATE);
            } else {
                ps.setDate(4, Date.valueOf(member.getDateOfBirth()));
            }

            if (member.getGender() == null) {
                ps.setNull(5, Types.NVARCHAR);
            } else {
                ps.setNString(5, member.getGender().getLabel());
            }

            ps.setInt(6, member.getId());
            return ps.executeUpdate() == 1;
        } catch (SQLException ex) {
            throw saveFailure(ex, member);
        }
    }

    /**
     * Marks a member as deleted without removing the record, using the supplied transaction.
     * Returns true when one profile is marked as deleted.
     */
    public boolean markDeleted(Connection conn, int id) throws SQLException {
        String sql = "UPDATE dbo.members SET is_deleted = 1 WHERE id = ? AND is_deleted = 0";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() == 1;
        }
    }

    /**
     * Restores a soft-deleted member profile and returns whether the record was updated.
     */
    public boolean restore(int id) {
        String sql = "UPDATE dbo.members SET is_deleted = 0 WHERE id = ? AND is_deleted = 1";

        try (Connection conn = DBContext.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() == 1;
        } catch (SQLException ex) {
            throw new AppException("Cannot restore member " + id, ex);
        }
    }

    /**
     * Converts duplicate-key errors into a phone validation error and wraps other database errors.
     */
    private RuntimeException saveFailure(SQLException ex, Member member) {
        for (SQLException cause = ex; cause != null; cause = cause.getNextException()) {
            int code = cause.getErrorCode();

            if (code == 2601 || code == 2627) {
                Member existing = null;
                try {
                    if (member.getPhone() != null) {
                        existing = findByPhone(member.getPhone());
                    }
                } catch (RuntimeException lookupFailure) {
                    // Preserve the duplicate error if the owner lookup fails.
                }

                String owner = existing == null ? "another profile" : existing.getCode();
                String message = "This phone number is already associated with member profile " + owner + ".";
                return new ValidationException(message, Map.of("phone", message));
            }
        }

        return new AppException("Cannot save member.", ex);
    }

    /**
     * Converts the current database result row into a Member object.
     */
    private Member mapRow(ResultSet rs) throws SQLException {
        Member member = new Member();
        member.setId(rs.getInt("id"));
        member.setFullName(rs.getString("full_name"));
        member.setPhone(rs.getString("phone"));
        member.setEmail(rs.getString("email"));
        member.setGender(Gender.fromLabel(rs.getString("gender")));
        member.setDeleted(rs.getBoolean("is_deleted"));
        member.setCreatedBy(rs.getInt("created_by"));

        Date dateOfBirth = rs.getDate("date_of_birth");
        if (dateOfBirth != null) {
            member.setDateOfBirth(dateOfBirth.toLocalDate());
        }

        Timestamp createdAt = rs.getTimestamp("created_at");
        if (createdAt != null) {
            member.setCreatedAt(createdAt.toLocalDateTime());
        }

        return member;
    }

    /**
     * Removes surrounding whitespace from the keyword and returns an empty string for null.
     */
    private String getKeyword(String keyword) {
        if (keyword == null) {
            return "";
        }
        return keyword.trim();
    }

    /**
     * SQL condition of a classified search keyword (FR-11), appended after the scope condition.
     * One placeholder for CODE, PHONE or NAME; none for NONE (matches nothing) or a blank keyword (no filter).
     */
    private String getKeywordCondition(MemberKeyword keyword) {
        if (keyword == null) {
            return "";
        }
        switch (keyword.getType()) {
            case CODE:
                return " AND m.id = ?";
            case PHONE:
                return " AND m.phone = ?";
            case NAME:
                return " AND m.full_name COLLATE Vietnamese_100_CI_AS LIKE ? ESCAPE '\\'";
            default:
                return " AND 1 = 0";
        }
    }

    /**
     * Binds the placeholder written by getKeywordCondition() and returns the next free parameter index.
     */
    private int bindKeyword(PreparedStatement ps, int index, MemberKeyword keyword) throws SQLException {
        if (keyword == null) {
            return index;
        }
        switch (keyword.getType()) {
            case CODE:
                ps.setInt(index++, keyword.getMemberId());
                break;
            case PHONE:
                ps.setString(index++, keyword.getValue());
                break;
            case NAME:
                ps.setNString(index++, keyword.getLikePattern());
                break;
            default:
                break;
        }
        return index;
    }

    /**
     * Returns the SQL filter for non-deleted, deleted, or all member profiles.
     */
    private String getScopeCondition(MemberScope scope) {
        if (scope == MemberScope.DELETED) {
            return "m.is_deleted = 1";
        } else if (scope == MemberScope.ALL) {
            return "1 = 1";
        } else {
            return "m.is_deleted = 0";
        }
    }
}
