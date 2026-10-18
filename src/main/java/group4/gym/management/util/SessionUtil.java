package group4.gym.management.util;

import group4.gym.management.model.Account;
import jakarta.servlet.http.HttpServletRequest;

/**
 * HttpSession/JSESSIONID helpers (CN-01, CN-02, CN-03).
 */
public final class SessionUtil {

    private SessionUtil() {
    }

    /**
     * Establish the session after a successful login.
     */
    public static void login(HttpServletRequest request, Account account) {
        // TODO: Create/get the session, call request.changeSessionId() to change the session ID (prevents session fixation),
        //       store AppConstants.SESSION_ACCOUNT_ID = account.getId(), setMaxInactiveInterval(60 minutes).
        // Output: nothing returned; the session holds the account ID.
    }

    /**
     * Invalidate the current session (logout, or when an account is locked/soft-deleted mid-session).
     */
    public static void invalidate(HttpServletRequest request) {
        // TODO: request.getSession(false); if it is not null, call invalidate().
        // Output: nothing returned; the old session can no longer reach protected features.
    }

    /**
     * Get the logged-in account ID from the session.
     */
    public static Integer getAccountId(HttpServletRequest request) {
        // TODO: Read the SESSION_ACCOUNT_ID attribute from the existing session (do not create a new session).
        // Output: the Integer ID; null if not logged in or the session expired.
        return null;
    }

    /**
     * Get the current account reloaded from the DB by AuthenticationFilter for this request.
     */
    public static Account getCurrentAccount(HttpServletRequest request) {
        // TODO: Read the request attribute AppConstants.REQUEST_CURRENT_ACCOUNT.
        // Output: the current Account; null if not authenticated.
        return null;
    }
}
