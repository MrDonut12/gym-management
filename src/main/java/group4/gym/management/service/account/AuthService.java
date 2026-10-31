package group4.gym.management.service.account;

import group4.gym.management.dao.account.AccountDAO;
import group4.gym.management.model.account.Account;
import group4.gym.management.model.account.AccountStatus;
import group4.gym.management.util.PasswordUtil;
import group4.gym.management.util.ValidationUtil;

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
        if (!ValidationUtil.isValidUsername(username) || rawPassword == null || rawPassword.isEmpty()) return null;
        Account account = accountDAO.findByUsername(username);
        return account != null && account.getStatus() == AccountStatus.ACTIVE
                && PasswordUtil.verify(rawPassword, account.getPasswordHash()) ? account : null;
    }

    /**
     * Re-read the account for AuthFilter on every request (CN-03). Lets database errors propagate so the filter denies the request.
     */
    public Account loadAccount(int accountId) {
        return accountDAO.findById(accountId);
    }
}
