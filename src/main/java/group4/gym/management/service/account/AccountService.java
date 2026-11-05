package group4.gym.management.service.account;

import group4.gym.management.dao.account.AccountDAO;
import group4.gym.management.model.account.Account;
import group4.gym.management.model.account.AccountStatus;
import group4.gym.management.model.account.Role;
import group4.gym.management.model.account.SessionUser;
import java.util.List;

/**
 * Staff account management (FR-05 to FR-10, BR-14, BR-16). Only ADMIN reaches these features (/admin/*),
 * except changeOwnPassword (every role). Invalid data throws ValidationException (section 6),
 * rule violations throw BusinessRuleException, unknown IDs throw NotFoundException.
 */
public class AccountService {

    private final AccountDAO accountDAO = new AccountDAO();

    /**
     * Account list (FR-05). A null criterion is ignored.
     */
    public List<Account> listAccounts(String keyword, Role role, AccountStatus status) {
        // TODO: Validate the keyword length (VR-07); accountDAO.search.
        // Output: list of Account.
        return null;
    }

    /**
     * Get an account to edit/lock/reset (VR-17).
     */
    public Account getById(int accountId) {
        // TODO: accountDAO.findById; null -> NotFoundException.
        // Output: the Account (passwordHash must not reach the view).
        return null;
    }

    /**
     * Create an account (FR-06), status ACTIVE.
     */
    public int createAccount(SessionUser actor, Account newAccount, String rawPassword, String confirmPassword) {
        // TODO: Validate VR-08 username (+ existsByUsername, case-insensitive), VR-01 full name, VR-03 phone (+ existsByPhone),
        //       VR-04 email, VR-10 role, VR-09 password and exact confirmation; any error -> ValidationException.
        //       PasswordUtil.hash; status = ACTIVE; createdAt = AppClock.now(); accountDAO.insert.
        // Output: ID of the new account.
        return 0;
    }

    /**
     * Update full name, phone, email, role (FR-07). The username is not editable.
     * BR-14: the actor cannot change their own role.
     */
    public void updateAccount(SessionUser actor, Account changes) {
        // TODO: getById; validate VR-01, VR-03 (existsByPhone excluding this id), VR-04, VR-10;
        //       changes.getId() == actor.getId() && role differs -> BusinessRuleException; accountDAO.update.
        //       A changed role logs that user out on the next request (AuthFilter, AC-03).
        // Output: nothing returned; throws an exception on failure.
    }

    /**
     * Lock or unlock an account (FR-08). BR-14: the actor cannot lock themselves. Accounts are never deleted.
     */
    public void setLocked(SessionUser actor, int targetId, boolean locked) {
        // TODO: getById; locked && targetId == actor.getId() -> BusinessRuleException; accountDAO.updateStatus(LOCKED/ACTIVE).
        // Output: nothing returned; throws an exception on failure.
    }

    /**
     * Change one's own password (FR-09, every role). The current session is kept.
     */
    public void changeOwnPassword(SessionUser actor, String currentPassword, String newPassword, String confirmPassword) {
        // TODO: accountDAO.findById(actor.getId()); PasswordUtil.verify(currentPassword) false -> field error;
        //       VR-09 on newPassword (not trimmed) and exact confirmation; PasswordUtil.hash; accountDAO.updatePasswordHash.
        // Output: nothing returned; throws an exception on failure.
    }

    /**
     * ADMIN sets a new password for another account (FR-10) without the old one.
     * BR-14: not allowed on oneself (use FR-09). The LOCKED/ACTIVE status is not changed (AC-08).
     */
    public void resetPassword(SessionUser actor, int targetId, String newPassword, String confirmPassword) {
        // TODO: targetId == actor.getId() -> BusinessRuleException; getById; VR-09 and confirmation; hash; accountDAO.updatePasswordHash.
        // Output: nothing returned; throws an exception on failure.
    }
}
