package group4.gym.management.dao;

import group4.gym.management.database.DBContext;
import group4.gym.management.exception.AppException;
import group4.gym.management.exception.BusinessRuleException;
import group4.gym.management.model.Account;
import group4.gym.management.model.enums.AccountStatus;
import group4.gym.management.model.enums.Role;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/** Staff accounts, including locked and soft-deleted records. */
public class AccountDAO {
    private static final String COLUMNS = "id, username, password_hash, full_name, phone, email, role, status, is_deleted, created_at, updated_at";
    private static final String NOW = "DATEADD(HOUR, 7, SYSUTCDATETIME())";

    public Account findById(int id) {
        return findOne("SELECT " + COLUMNS + " FROM accounts WHERE id = ?", id);
    }

    public Account findByUsername(String username) {
        return findOne("SELECT " + COLUMNS + " FROM accounts WHERE LOWER(username) = LOWER(?)", username);
    }

    public List<Account> search(String keyword, List<Role> roles, AccountStatus status, boolean includeDeleted) {
        List<Account> result = new ArrayList<>();
        if (roles != null && roles.isEmpty()) return result;
        StringBuilder sql = new StringBuilder("SELECT " + COLUMNS + " FROM accounts WHERE 1 = 1");
        List<Object> parameters = new ArrayList<>();
        if (!includeDeleted) sql.append(" AND is_deleted = 0");
        if (keyword != null && !keyword.trim().isEmpty()) {
            sql.append(" AND (LOWER(username) LIKE LOWER(?) ESCAPE '!' OR LOWER(full_name) LIKE LOWER(?) ESCAPE '!' OR phone LIKE ? ESCAPE '!')");
            String term = "%" + keyword.trim().replace("!", "!!").replace("%", "!%")
                    .replace("_", "!_").replace("[", "![") + "%";
            parameters.add(term);
            parameters.add(term);
            parameters.add(term);
        }
        if (roles != null) {
            sql.append(" AND role IN (");
            for (int i = 0; i < roles.size(); i++) {
                if (i > 0) sql.append(",");
                sql.append("?");
                parameters.add(roles.get(i).name());
            }
            sql.append(")");
        }
        if (status != null) {
            sql.append(" AND status = ?");
            parameters.add(status.name());
        }
        sql.append(" ORDER BY id");
        try (Connection connection = openConnection(); PreparedStatement statement = connection.prepareStatement(sql.toString())) {
            for (int i = 0; i < parameters.size(); i++) statement.setObject(i + 1, parameters.get(i));
            try (ResultSet rows = statement.executeQuery()) {
                while (rows.next()) {
                    Account account = mapRow(rows);
                    account.setPasswordHash(null);
                    result.add(account);
                }
            }
            return result;
        } catch (SQLException ex) {
            throw databaseError(ex);
        }
    }

    public boolean existsByUsername(String username, Integer excludeId) {
        return exists("LOWER(username) = LOWER(?)", username, excludeId);
    }

    public boolean existsByPhone(String phone, Integer excludeId) {
        return exists("phone = ?", phone, excludeId);
    }

    public int insert(Account account) {
        String sql = "INSERT INTO accounts (username, password_hash, full_name, phone, email, role, status, is_deleted) VALUES (?, ?, ?, ?, ?, ?, ?, 0)";
        try (Connection connection = openConnection();
                PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, account.getUsername());
            statement.setString(2, account.getPasswordHash());
            statement.setString(3, account.getFullName());
            statement.setString(4, account.getPhone());
            statement.setString(5, account.getEmail());
            statement.setString(6, account.getRole().name());
            statement.setString(7, account.getStatus().name());
            if (statement.executeUpdate() != 1) throw new SQLException("Account insert did not affect one row");
            try (ResultSet keys = statement.getGeneratedKeys()) {
                if (keys.next()) return keys.getInt(1);
                throw new SQLException("Account insert returned no ID");
            }
        } catch (SQLException ex) {
            throw databaseError(ex);
        }
    }

    public boolean updateProfile(Account account) {
        return update("UPDATE accounts SET full_name = ?, phone = ?, email = ?, updated_at = " + NOW
                + " WHERE id = ? AND is_deleted = 0", account.getFullName(), account.getPhone(), account.getEmail(), account.getId());
    }

    public boolean updateRole(int id, Role role) {
        return update("UPDATE accounts SET role = ?, updated_at = " + NOW + " WHERE id = ? AND is_deleted = 0", role.name(), id);
    }

    public boolean updateStatus(int id, AccountStatus status) {
        return update("UPDATE accounts SET status = ?, updated_at = " + NOW + " WHERE id = ? AND is_deleted = 0", status.name(), id);
    }

    public boolean softDelete(int id) {
        return update("UPDATE accounts SET is_deleted = 1, deleted_at = " + NOW + ", updated_at = " + NOW
                + " WHERE id = ? AND is_deleted = 0", id);
    }

    public boolean updatePasswordHash(int id, String passwordHash) {
        return update("UPDATE accounts SET password_hash = ?, updated_at = " + NOW
                + " WHERE id = ? AND is_deleted = 0", passwordHash, id);
    }

    private Account findOne(String sql, Object value) {
        try (Connection connection = openConnection(); PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setObject(1, value);
            try (ResultSet rows = statement.executeQuery()) {
                return rows.next() ? mapRow(rows) : null;
            }
        } catch (SQLException ex) {
            throw databaseError(ex);
        }
    }

    private boolean exists(String condition, String value, Integer excludeId) {
        String sql = "SELECT COUNT(*) FROM accounts WHERE " + condition + (excludeId == null ? "" : " AND id <> ?");
        try (Connection connection = openConnection(); PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, value);
            if (excludeId != null) statement.setInt(2, excludeId);
            try (ResultSet rows = statement.executeQuery()) {
                rows.next();
                return rows.getInt(1) > 0;
            }
        } catch (SQLException ex) {
            throw databaseError(ex);
        }
    }

    private boolean update(String sql, Object... parameters) {
        try (Connection connection = openConnection(); PreparedStatement statement = connection.prepareStatement(sql)) {
            for (int i = 0; i < parameters.length; i++) statement.setObject(i + 1, parameters[i]);
            return statement.executeUpdate() == 1;
        } catch (SQLException ex) {
            throw databaseError(ex);
        }
    }

    private Connection openConnection() {
        Connection connection = new DBContext().getConnection();
        if (connection == null) throw new AppException("Không thể kết nối cơ sở dữ liệu.");
        return connection;
    }

    private AppException databaseError(SQLException ex) {
        // The unique constraints also protect against simultaneous creates/updates.
        if (ex.getErrorCode() == 2601 || ex.getErrorCode() == 2627) {
            return new BusinessRuleException("Tên đăng nhập hoặc số điện thoại đã tồn tại.");
        }
        return new AppException("Không thể đọc hoặc lưu tài khoản.", ex);
    }

    private Account mapRow(ResultSet rs) throws SQLException {
        Account account = new Account();
        account.setId(rs.getInt("id"));
        account.setUsername(rs.getString("username"));
        account.setPasswordHash(rs.getString("password_hash"));
        account.setFullName(rs.getString("full_name"));
        account.setPhone(rs.getString("phone"));
        account.setEmail(rs.getString("email"));
        account.setRole(Role.valueOf(rs.getString("role")));
        account.setStatus(AccountStatus.valueOf(rs.getString("status")));
        account.setDeleted(rs.getBoolean("is_deleted"));
        Timestamp created = rs.getTimestamp("created_at");
        Timestamp updated = rs.getTimestamp("updated_at");
        account.setCreatedAt(created == null ? null : created.toLocalDateTime());
        account.setUpdatedAt(updated == null ? null : updated.toLocalDateTime());
        return account;
    }
}
