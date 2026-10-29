package group4.gym.management.util;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Remember-username cookie (CN-04, QT-17, CL-05).
 * Contains only the username; never a password, role or any authentication data.
 * The cookie never logs the user in: the password must always be entered.
 */
public final class CookieUtil {

    /** Cookie name. */
    public static final String REMEMBER_USERNAME_COOKIE = "gym_username";

    /** 30-day lifetime (seconds). */
    public static final int REMEMBER_USERNAME_MAX_AGE = 30 * 24 * 60 * 60;

    private CookieUtil() {
    }

    /**
     * Send the gym_username cookie after a successful login with "Remember username" checked.
     */
    public static void addRememberUsernameCookie(HttpServletRequest request, HttpServletResponse response, String username) {
        if (!ValidationUtil.isValidUsername(username)) {
            throw new IllegalArgumentException("Invalid username");
        }
        Cookie cookie = new Cookie(REMEMBER_USERNAME_COOKIE, username);
        cookie.setMaxAge(REMEMBER_USERNAME_MAX_AGE);
        cookie.setPath(request.getContextPath().isEmpty() ? "/" : request.getContextPath());
        cookie.setHttpOnly(true);
        response.addCookie(cookie);
    }

    /**
     * Delete the gym_username cookie after a successful login with the box unchecked (Max-Age = 0).
     * Must not be called on a failed login or on logout (QT-17).
     */
    public static void clearRememberUsernameCookie(HttpServletRequest request, HttpServletResponse response) {
        Cookie cookie = new Cookie(REMEMBER_USERNAME_COOKIE, "");
        cookie.setMaxAge(0);
        cookie.setPath(request.getContextPath().isEmpty() ? "/" : request.getContextPath());
        cookie.setHttpOnly(true);
        response.addCookie(cookie);
    }

    /**
     * Read the remembered username to pre-fill the login page.
     */
    public static String getRememberedUsername(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (REMEMBER_USERNAME_COOKIE.equals(cookie.getName())
                        && ValidationUtil.isValidUsername(cookie.getValue())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }
}
