package group4.gym.management.dao.account;

import group4.gym.management.model.account.Account;
import group4.gym.management.model.account.AccountStatus;
import group4.gym.management.model.account.Role;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * Data access for staff accounts (table accounts, DL-01). No physical deletion (QT-16).
 * Every query uses PreparedStatement parameters (CL-02). Methods open their own connection with
 * DBContext.getConnection() and wrap SQLException in AppException.
 */
public class AccountDAO {

    /**
     * Find an account by ID (used by AuthFilter on every request, CN-03).
     */
    public Account findById(int id) {
        // TODO: SELECT ... FROM accounts WHERE id = ?.
        // Output: Account; null if it does not exist.
        return null;
    }

    /**
     * Find an account by username, case-insensitive (CN-01). The column uses a case-insensitive collation (DL-01).
     */
    public Account findByUsername(String username) {
        // TODO: SELECT ... FROM accounts WHERE username = ?.
        // Output: Account; null if it does not exist.
        return null;
    }

    /**
     * Account list (CN-05). A null parameter means the criterion is ignored.
     *
     * @param keyword partial match on username or full name, case-insensitive
     */
    public List<Account> search(String keyword, Role role, AccountStatus status) {
        // TODO: Dynamic WHERE built from fixed fragments with parameters: (username LIKE ? OR full_name LIKE ?) [AND role = ?] [AND status = ?];
        //       ORDER BY created_at DESC.
        // Output: list of Account (passwordHash must never reach the view).
        return null;
    }

    /**
     * Whether the username already exists, case-insensitive (HL-08).
     */
    public boolean existsByUsername(String username) {
        // TODO: SELECT COUNT(*) FROM accounts WHERE username = ?.
        // Output: true if duplicated.
        return false;
    }

    /**
     * Whether the phone already exists within accounts (HL-03). It may equal a member's phone.
     *
     * @param excludeId ignore this account when updating; null when creating
     */
    public boolean existsByPhone(String phone, Integer excludeId) {
        // TODO: SELECT COUNT(*) FROM accounts WHERE phone = ? [AND id <> ?].
        // Output: true if duplicated.
        return false;
    }

    /**
     * Insert a new account (CN-06).
     */
    public int insert(Account account) {
        // TODO: INSERT (username, password_hash, full_name, phone, email, role, status, created_at).
        // Output: the generated ID of the new record.
        return 0;
    }

    /**
     * Update full name, phone, email and role (CN-07). The username is never updated.
     */
    public boolean update(Account account) {
        // TODO: UPDATE accounts SET full_name = ?, phone = ?, email = ?, role = ? WHERE id = ?.
        // Output: true if exactly 1 row was updated.
        return false;
    }

    /**
     * Lock/unlock an account (CN-08).
     */
    public boolean updateStatus(int id, AccountStatus status) {
        // TODO: UPDATE accounts SET status = ? WHERE id = ?.
        // Output: true if exactly 1 row was updated.
        return false;
    }

    /**
     * Update the password hash (CN-09 own change, CN-10 ADMIN reset). Does not touch the status.
     */
    public boolean updatePasswordHash(int id, String passwordHash) {
        // TODO: UPDATE accounts SET password_hash = ? WHERE id = ?.
        // Output: true if exactly 1 row was updated.
        return false;
    }

    /**
     * Map one ResultSet row to an Account.
     */
    private Account mapRow(ResultSet rs) throws SQLException {
        // TODO: Read the columns and assign them to an Account.
        // Output: Account.
        return null;
    }
}
