package group4.gym.management.service;

import group4.gym.management.dao.AccountDAO;
import group4.gym.management.model.Account;

/**
 * Login authentication and per-request account status checks (CN-01, CN-03, CN-04).
 */
public class AuthService {

    private final AccountDAO accountDAO = new AccountDAO();

    /**
     * Authenticate a username/password.
     * LOCKED or soft-deleted accounts are rejected; the error message is generic and does not reveal whether the username exists.
     */
    public Account authenticate(String username, String rawPassword) {
        // TODO: accountDAO.findByUsername; check it exists, status is ACTIVE and it is not soft-deleted; PasswordUtil.verify.
        // Output: the Account if the login is valid; null otherwise.
        return null;
    }

    /**
     * Reload the account from the DB for a protected request; returns it only while the account is ACTIVE and not soft-deleted.
     * AuthenticationFilter treats a null result as a reason to invalidate the session (session table in section 3.1.1).
     */
    public Account loadActiveAccount(int accountId) {
        // TODO: accountDAO.findById; check status ACTIVE and !deleted.
        // Output: the valid Account; null if locked/soft-deleted/not found.
        return null;
    }
}
