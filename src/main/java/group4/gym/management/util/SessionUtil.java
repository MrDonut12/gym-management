package group4.gym.management.util;

import jakarta.servlet.http.HttpSession;

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
        invalidate(request);
        HttpSession session = request.getSession(true);
        session.setAttribute(AppConstants.SESSION_ACCOUNT_ID, account.getId());
        session.setMaxInactiveInterval(AppConstants.SESSION_TIMEOUT_MINUTES * 60);
    }

    /**
     * Invalidate the current session (logout, or when an account is locked/soft-deleted mid-session).
     */
    public static void invalidate(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) session.invalidate();
    }

    /**
     * Get the logged-in account ID from the session.
     */
    public static Integer getAccountId(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        Object id = session == null ? null : session.getAttribute(AppConstants.SESSION_ACCOUNT_ID);
        return id instanceof Integer ? (Integer) id : null;
    }

    /**
     * Get the current account reloaded from the DB by AuthenticationFilter for this request.
     */
    public static Account getCurrentAccount(HttpServletRequest request) {
        return (Account) request.getAttribute(AppConstants.REQUEST_CURRENT_ACCOUNT);
    }
}
