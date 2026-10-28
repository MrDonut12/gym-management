package group4.gym.management.util;

import group4.gym.management.model.account.Account;
import group4.gym.management.model.account.SessionUser;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

/**
 * HttpSession helpers (CN-01, CN-02, CN-03) and flash messages for Post/Redirect/Get (CL-07).
 * Sessions are never stored in the database.
 */
public final class SessionUtil {

    private SessionUtil() {
    }

    /**
     * Establish the session after a successful login (CN-01).
     */
    public static void login(HttpServletRequest request, Account account) {
        HttpSession session = request.getSession(true);
        request.changeSessionId();
        session.setAttribute(AppConstants.SESSION_USER,
                new SessionUser(account.getId(), account.getUsername(), account.getFullName(), account.getRole()));
        session.setMaxInactiveInterval(AppConstants.SESSION_TIMEOUT_MINUTES * 60);
    }

    /**
     * Invalidate the current session (logout, or when AuthFilter detects LOCKED / changed role / DB error).
     */
    public static void invalidate(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
    }

    /**
     * Get the logged-in user from the session.
     */
    public static SessionUser getSessionUser(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        return session == null ? null : (SessionUser) session.getAttribute(AppConstants.SESSION_USER);
    }

    /**
     * Store a success message shown once on the page after the redirect (CL-07). common/messages.jsp displays and removes it.
     */
    public static void flashMessage(HttpServletRequest request, String message) {
        request.getSession().setAttribute(AppConstants.FLASH_MESSAGE, message);
    }

    /**
     * Store an error message shown once on the page after the redirect (CL-07).
     */
    public static void flashError(HttpServletRequest request, String message) {
        request.getSession().setAttribute(AppConstants.FLASH_ERROR, message);
    }
}
