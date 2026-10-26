package group4.gym.management.service.account;

import group4.gym.management.dao.account.AccountDAO;
import group4.gym.management.model.account.Account;
import group4.gym.management.model.account.AccountStatus;
import group4.gym.management.model.account.Role;
import group4.gym.management.model.account.SessionUser;
import java.util.List;

/**
 * Staff account management (CN-05 to CN-10, QT-14, QT-16). Only ADMIN reaches these features (/admin/*),
 * except changeOwnPassword (every role). Invalid data throws ValidationException (section 6),
 * rule violations throw BusinessRuleException, unknown IDs throw NotFoundException.
 */
public class AccountService {

    private final AccountDAO accountDAO = new AccountDAO();

    /**
     * Account list (CN-05). A null criterion is ignored.
     */
    public List<Account> listAccounts(String keyword, Role role, AccountStatus status) {
        // TODO: Validate the keyword length (HL-07); accountDAO.search.
        // Output: list of Account.
        return null;
    }

    /**
     * Get an account to edit/lock/reset (HL-17).
     */
    public Account getById(int accountId) {
        // TODO: accountDAO.findById; null -> NotFoundException.
        // Output: the Account (passwordHash must not reach the view).
        return null;
    }

    /**
     * Create an account (CN-06), status ACTIVE.
     */
    public int createAccount(SessionUser actor, Account newAccount, String rawPassword, String confirmPassword) {
        // TODO: Validate HL-08 username (+ existsByUsername, case-insensitive), HL-01 full name, HL-03 phone (+ existsByPhone),
        //       HL-04 email, HL-10 role, HL-09 password and exact confirmation; any error -> ValidationException.
        //       PasswordUtil.hash; status = ACTIVE; createdAt = AppClock.now(); accountDAO.insert.
        // Output: ID of the new account.
        return 0;
    }

    /**
     * Update full name, phone, email, role (CN-07). The username is not editable.
     * QT-14: the actor cannot change their own role.
     */
    public void updateAccount(SessionUser actor, Account changes) {
        // TODO: getById; validate HL-01, HL-03 (existsByPhone excluding this id), HL-04, HL-10;
        //       changes.getId() == actor.getId() && role differs -> BusinessRuleException; accountDAO.update.
        //       A changed role logs that user out on the next request (AuthFilter, KT-03).
        // Output: nothing returned; throws an exception on failure.
    }

    /**
     * Lock or unlock an account (CN-08). QT-14: the actor cannot lock themselves. Accounts are never deleted.
     */
    public void setLocked(SessionUser actor, int targetId, boolean locked) {
        // TODO: getById; locked && targetId == actor.getId() -> BusinessRuleException; accountDAO.updateStatus(LOCKED/ACTIVE).
        // Output: nothing returned; throws an exception on failure.
    }

    /**
     * Change one's own password (CN-09, every role). The current session is kept.
     */
    public void changeOwnPassword(SessionUser actor, String currentPassword, String newPassword, String confirmPassword) {
        // TODO: accountDAO.findById(actor.getId()); PasswordUtil.verify(currentPassword) false -> field error;
        //       HL-09 on newPassword (not trimmed) and exact confirmation; PasswordUtil.hash; accountDAO.updatePasswordHash.
        // Output: nothing returned; throws an exception on failure.
    }

    /**
     * ADMIN sets a new password for another account (CN-10) without the old one.
     * QT-14: not allowed on oneself (use CN-09). The LOCKED/ACTIVE status is not changed (KT-08).
     */
    public void resetPassword(SessionUser actor, int targetId, String newPassword, String confirmPassword) {
        // TODO: targetId == actor.getId() -> BusinessRuleException; getById; HL-09 and confirmation; hash; accountDAO.updatePasswordHash.
        // Output: nothing returned; throws an exception on failure.
    }
}
