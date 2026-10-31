package group4.gym.management.dao.account;

import group4.gym.management.database.DBContext;
import group4.gym.management.exception.AppException;
import group4.gym.management.exception.ValidationException;
import group4.gym.management.model.account.Account;
import group4.gym.management.model.account.AccountStatus;
import group4.gym.management.model.account.Role;
import java.sql.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/** SQL access for accounts; accounts are never physically deleted. */
public class AccountDAO {
    public Account findById(int id) {
        return find("SELECT * FROM accounts WHERE id = ?", id);
    }

    public Account findByUsername(String username) {
        return find("SELECT * FROM accounts WHERE username = ?", username);
    }

    private Account find(String sql, Object value) {
        try (Connection c = DBContext.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setObject(1, value);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? mapRow(rs) : null;
            }
        } catch (SQLException ex) {
            throw new AppException("Cannot read account.", ex);
        }
    }

    public List<Account> search(String keyword, Role role, AccountStatus status) {
        StringBuilder sql = new StringBuilder("SELECT * FROM accounts WHERE 1 = 1");
        List<Object> parameters = new ArrayList<>();
        if (keyword != null && !keyword.isEmpty()) {
            sql.append(" AND (username LIKE ? ESCAPE '!' OR full_name COLLATE Latin1_General_100_CI_AS LIKE ? ESCAPE '!')");
            String pattern = "%" + keyword.replace("!", "!!").replace("%", "!%")
                    .replace("_", "!_").replace("[", "![") + "%";
            parameters.add(pattern);
            parameters.add(pattern);
        }
        if (role != null) {
            sql.append(" AND role = ?");
            parameters.add(role.name());
        }
        if (status != null) {
            sql.append(" AND status = ?");
            parameters.add(status.name());
        }
        sql.append(" ORDER BY created_at DESC, id DESC");
        try (Connection c = DBContext.getConnection(); PreparedStatement ps = c.prepareStatement(sql.toString())) {
            for (int i = 0; i < parameters.size(); i++) ps.setObject(i + 1, parameters.get(i));
            try (ResultSet rs = ps.executeQuery()) {
                List<Account> accounts = new ArrayList<>();
                while (rs.next()) {
                    Account account = mapRow(rs);
                    account.setPasswordHash(null);
                    accounts.add(account);
                }
                return accounts;
            }
        } catch (SQLException ex) {
            throw new AppException("Cannot list accounts.", ex);
        }
    }

    public boolean existsByUsername(String username) {
        return findByUsername(username) != null;
    }

    public boolean existsByPhone(String phone, Integer excludeId) {
        String sql = "SELECT 1 FROM accounts WHERE phone = ?" + (excludeId == null ? "" : " AND id <> ?");
        try (Connection c = DBContext.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, phone);
            if (excludeId != null) ps.setInt(2, excludeId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException ex) {
            throw new AppException("Cannot check phone.", ex);
        }
    }

    public int insert(Account account) {
        String sql = "INSERT INTO accounts (username, password_hash, full_name, phone, email, role, status, created_at) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection c = DBContext.getConnection(); PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, account.getUsername());
            ps.setString(2, account.getPasswordHash());
            ps.setString(3, account.getFullName());
            ps.setString(4, account.getPhone());
            ps.setString(5, account.getEmail());
            ps.setString(6, account.getRole().name());
            ps.setString(7, account.getStatus().name());
            ps.setTimestamp(8, Timestamp.valueOf(account.getCreatedAt()));
            if (ps.executeUpdate() != 1) throw new SQLException("Account insert affected no row.");
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) return rs.getInt(1);
                throw new SQLException("Missing generated account ID.");
            }
        } catch (SQLException ex) {
            throw writeFailure(ex, account, true);
        }
    }

    public boolean update(Account account) {
        try {
            return executeUpdate("UPDATE accounts SET full_name = ?, phone = ?, email = ?, role = ? WHERE id = ?",
                    account.getFullName(), account.getPhone(), account.getEmail(), account.getRole().name(), account.getId());
        } catch (SQLException ex) {
            throw writeFailure(ex, account, false);
        }
    }

    public boolean updateStatus(int id, AccountStatus status) {
        try {
            return executeUpdate("UPDATE accounts SET status = ? WHERE id = ?", status.name(), id);
        } catch (SQLException ex) {
            throw new AppException("Cannot update account status.", ex);
        }
    }

    public boolean updatePasswordHash(int id, String passwordHash) {
        try {
            return executeUpdate("UPDATE accounts SET password_hash = ? WHERE id = ?", passwordHash, id);
        } catch (SQLException ex) {
            throw new AppException("Cannot update password.", ex);
        }
    }

    private boolean executeUpdate(String sql, Object... parameters) throws SQLException {
        try (Connection c = DBContext.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            for (int i = 0; i < parameters.length; i++) ps.setObject(i + 1, parameters[i]);
            return ps.executeUpdate() == 1;
        }
    }

    private AppException writeFailure(SQLException ex, Account account, boolean creating) {
        for (SQLException cause = ex; cause != null; cause = cause.getNextException()) {
            if (cause.getErrorCode() == 2601 || cause.getErrorCode() == 2627) {
                Map<String, String> errors = new LinkedHashMap<>();
                if (creating && existsByUsername(account.getUsername())) errors.put("username", "Username already exists.");
                if (existsByPhone(account.getPhone(), creating ? null : account.getId())) errors.put("phone", "Phone already belongs to another account.");
                return new ValidationException("Account data conflicts with another account. Please review and retry.", errors);
            }
        }
        return new AppException("Cannot save account.", ex);
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
        account.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        return account;
    }
}
