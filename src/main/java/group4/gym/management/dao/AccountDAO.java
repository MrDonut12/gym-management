package group4.gym.management.dao;

import group4.gym.management.model.Account;
import group4.gym.management.model.enums.AccountStatus;
import group4.gym.management.model.enums.Role;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * Data access for staff accounts (DL-07). No physical deletion.
 */
public class AccountDAO {

    /**
     * Find an account by ID (including locked/soft-deleted).
     */
    public Account findById(int id) {
        // TODO: SELECT by id.
        // Output: Account; null if it does not exist.
        return null;
    }

    /**
     * Find an account by username, case-insensitive (including locked/soft-deleted).
     */
    public Account findByUsername(String username) {
        // TODO: SELECT ... WHERE LOWER(username) = LOWER(?).
        // Output: Account; null if it does not exist.
        return null;
    }

    /**
     * Search accounts by criteria (a null parameter means the criterion is ignored).
     *
     * @param keyword        partial match on username/full name/phone
     * @param roles          only these roles (the viewer's management scope)
     * @param status         ACTIVE/LOCKED
     * @param includeDeleted whether to include soft-deleted accounts
     */
    public List<Account> search(String keyword, List<Role> roles, AccountStatus status, boolean includeDeleted) {
        // TODO: Build a dynamic SELECT with a PreparedStatement.
        // Output: list of Account (passwordHash must never be passed to the view).
        return null;
    }

    /**
     * Whether the username already exists (case-insensitive, including locked/soft-deleted).
     *
     * @param excludeId ignore this account when updating; null when creating
     */
    public boolean existsByUsername(String username, Integer excludeId) {
        // TODO: SELECT COUNT(*) ... WHERE LOWER(username) = LOWER(?) [AND id <> ?].
        // Output: true if duplicated.
        return false;
    }

    /**
     * Whether the phone already exists within the staff set (including locked/soft-deleted). It may equal a member's phone.
     */
    public boolean existsByPhone(String phone, Integer excludeId) {
        // TODO: SELECT COUNT(*) ... WHERE phone = ? [AND id <> ?].
        // Output: true if duplicated.
        return false;
    }

    /**
     * Insert a new account.
     */
    public int insert(Account account) {
        // TODO: INSERT (username, password_hash, full_name, phone, email, role, status, is_deleted, created_at).
        // Output: the generated ID of the new record.
        return 0;
    }

    /**
     * Update basic information (full name, phone, email). Password/role are not updated here.
     */
    public boolean updateProfile(Account account) {
        // TODO: UPDATE full_name, phone, email, updated_at WHERE id = ? AND is_deleted = 0.
        // Output: true if exactly 1 row was updated.
        return false;
    }

    /**
     * Update the role (call only after RbacPolicy.canAssignRole has been checked).
     */
    public boolean updateRole(int id, Role role) {
        // TODO: UPDATE role, updated_at WHERE id = ? AND is_deleted = 0.
        // Output: true if exactly 1 row was updated.
        return false;
    }

    /**
     * Lock/unlock an account.
     */
    public boolean updateStatus(int id, AccountStatus status) {
        // TODO: UPDATE status, updated_at WHERE id = ? AND is_deleted = 0.
        // Output: true if exactly 1 row was updated.
        return false;
    }

    /**
     * Soft-delete an account; keeps links to transactions/visits.
     */
    public boolean softDelete(int id) {
        // TODO: UPDATE is_deleted = 1, updated_at WHERE id = ? AND is_deleted = 0.
        // Output: true if exactly 1 row was updated.
        return false;
    }

    /**
     * Update the password hash (self change or Administrator reset).
     */
    public boolean updatePasswordHash(int id, String passwordHash) {
        // TODO: UPDATE password_hash, updated_at WHERE id = ?.
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
