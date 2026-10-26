package group4.gym.management.service.account;

import group4.gym.management.dao.account.AccountDAO;
import group4.gym.management.model.account.Account;

/**
 * Login authentication and the per-request account check of AuthFilter (CN-01, CN-03).
 */
public class AuthService {

    private final AccountDAO accountDAO = new AccountDAO();

    /**
     * Authenticate a username/password (CN-01). The username is case-insensitive; the password is compared with the hash (CL-03).
     * Every failure (unknown username, wrong password, LOCKED) gives the same result so the servlet shows one generic message.
     */
    public Account authenticate(String username, String rawPassword) {
        // TODO: Blank username/password -> null; accountDAO.findByUsername; must exist, status ACTIVE, PasswordUtil.verify.
        // Output: the Account if the login is valid; null otherwise.
        return null;
    }

    /**
     * Re-read the account for AuthFilter on every request (CN-03). Lets database errors propagate so the filter denies the request.
     */
    public Account loadAccount(int accountId) {
        // TODO: accountDAO.findById(accountId).
        // Output: the Account (the filter compares status and role); null if it does not exist.
        return null;
    }
}
