package group4.gym.management.service;

import group4.gym.management.dao.AccountDAO;
import group4.gym.management.model.Account;
import group4.gym.management.model.enums.AccountStatus;
import group4.gym.management.model.enums.Role;
import java.util.List;

/**
 * Account and staff management (CN-04, CN-35, CN-36).
 * Every method checks scope with RbacPolicy; violations throw AccessDeniedException,
 * invalid data throws ValidationException, business rule violations throw BusinessRuleException.
 */
public class AccountService {

    private final AccountDAO accountDAO = new AccountDAO();

    /**
     * List the accounts within the actor's management scope
     * (Administrator: all; Manager: only Receptionist/Accountant).
     */
    public List<Account> listManageableAccounts(Account actor, String keyword, Role role, AccountStatus status,
            boolean includeDeleted) {
        // TODO: Determine the roles the actor may manage and call accountDAO.search.
        // Output: list of Account within the scope.
        return null;
    }

    /**
     * Get an account to view/edit, checking it is within the management scope.
     */
    public Account getManageableAccount(Account actor, int targetId) {
        // TODO: accountDAO.findById; RbacPolicy.canManageAccount; not found -> BusinessRuleException.
        // Output: the target Account (passwordHash must not be used in views).
        return null;
    }

    /**
     * Create an account (CN-04, CN-35). A Manager may only create Receptionist/Accountant accounts.
     */
    public int createAccount(Account actor, Account newAccount, String rawPassword, String confirmPassword) {
        // TODO: ValidationUtil.validateAccount + validateNewPassword; check duplicate username (case-insensitive)
        //       and phone within the staff set; RbacPolicy.canManageRole; hash the password; status ACTIVE; insert.
        // Output: ID of the new account.
        return 0;
    }

    /**
     * Update basic staff information (full name, phone, email).
     */
    public void updateAccount(Account actor, Account changes) {
        // TODO: getManageableAccount; validate; check duplicate phone (excludeId); accountDAO.updateProfile.
        // Output: nothing returned; throws an exception on failure.
    }

    /**
     * Assign/change a role (Administrator only, privilege self-escalation forbidden).
     */
    public void changeRole(Account actor, int targetId, Role newRole) {
        // TODO: RbacPolicy.canAssignRole; accountDAO.updateRole.
        // Output: nothing returned; throws an exception on failure.
    }

    /**
     * Lock an account. Existing sessions are blocked on the next protected request (AuthenticationFilter).
     */
    public void lockAccount(Account actor, int targetId) {
        // TODO: getManageableAccount; forbid locking oneself; accountDAO.updateStatus(LOCKED).
        // Output: nothing returned; throws an exception on failure.
    }

    /**
     * Unlock an account. Does not restore sessions that were already invalidated.
     */
    public void unlockAccount(Account actor, int targetId) {
        // TODO: getManageableAccount; accountDAO.updateStatus(ACTIVE).
        // Output: nothing returned; throws an exception on failure.
    }

    /**
     * Soft-delete an account (Administrator only, self-deletion forbidden, history is kept).
     */
    public void softDeleteAccount(Account actor, int targetId) {
        // TODO: Check the SOFT_DELETE_ACCOUNT permission and that target differs from actor; accountDAO.softDelete.
        // Output: nothing returned; throws an exception on failure.
    }

    /**
     * Change one's own password after valid authentication (every role, CN-36).
     */
    public void changeOwnPassword(Account actor, String currentPassword, String newPassword, String confirmPassword) {
        // TODO: Reload actor from the DB (ACTIVE, not soft-deleted); PasswordUtil.verify(currentPassword);
        //       validateNewPassword; hash and accountDAO.updatePasswordHash.
        // Output: nothing returned; throws an exception on failure.
    }

    /**
     * An Administrator resets another account's password within scope (CN-36). Other roles are rejected.
     */
    public void resetPassword(Account actor, int targetId, String newPassword, String confirmPassword) {
        // TODO: RbacPolicy.canResetPassword; validateNewPassword; hash and accountDAO.updatePasswordHash.
        // Output: nothing returned; throws an exception on failure.
    }
}
