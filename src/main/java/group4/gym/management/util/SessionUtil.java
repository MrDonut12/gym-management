package group4.gym.management.util;

import group4.gym.management.model.account.Account;
import group4.gym.management.model.account.SessionUser;
import jakarta.servlet.http.HttpServletRequest;

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
        // TODO: request.getSession(true); request.changeSessionId() (prevents session fixation, KT-01);
        //       store AppConstants.SESSION_USER = new SessionUser(id, username, fullName, role);
        //       setMaxInactiveInterval(AppConstants.SESSION_TIMEOUT_MINUTES * 60).
        // Output: nothing returned; the session holds the SessionUser.
    }

    /**
     * Invalidate the current session (logout, or when AuthFilter detects LOCKED / changed role / DB error).
     */
    public static void invalidate(HttpServletRequest request) {
        // TODO: request.getSession(false); if it is not null, call invalidate().
        // Output: nothing returned; the old session can no longer reach protected features.
    }

    /**
     * Get the logged-in user from the session.
     */
    public static SessionUser getSessionUser(HttpServletRequest request) {
        // TODO: Read AppConstants.SESSION_USER from the existing session (do not create a new session).
        // Output: the SessionUser; null if not logged in or the session expired.
        return null;
    }

    /**
     * Store a success message shown once on the page after the redirect (CL-07). common/messages.jsp displays and removes it.
     */
    public static void flashMessage(HttpServletRequest request, String message) {
        // TODO: request.getSession().setAttribute(AppConstants.FLASH_MESSAGE, message).
        // Output: nothing returned.
    }

    /**
     * Store an error message shown once on the page after the redirect (CL-07).
     */
    public static void flashError(HttpServletRequest request, String message) {
        // TODO: request.getSession().setAttribute(AppConstants.FLASH_ERROR, message).
        // Output: nothing returned.
    }
}
