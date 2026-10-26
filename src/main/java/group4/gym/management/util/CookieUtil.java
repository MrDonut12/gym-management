package group4.gym.management.util;

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
        // TODO: new Cookie(REMEMBER_USERNAME_COOKIE, username) (HL-08 characters are cookie-safe);
        //       MaxAge = REMEMBER_USERNAME_MAX_AGE; Path = request.getContextPath(); HttpOnly = true; response.addCookie(...).
        // Output: nothing returned; the response carries a Set-Cookie header with the correct attributes.
    }

    /**
     * Delete the gym_username cookie after a successful login with the box unchecked (Max-Age = 0).
     * Must not be called on a failed login or on logout (QT-17).
     */
    public static void clearRememberUsernameCookie(HttpServletRequest request, HttpServletResponse response) {
        // TODO: Cookie with the same name and Path, empty value, MaxAge = 0, HttpOnly; response.addCookie(...).
        // Output: nothing returned; the browser removes the cookie.
    }

    /**
     * Read the remembered username to pre-fill the login page.
     */
    public static String getRememberedUsername(HttpServletRequest request) {
        // TODO: Iterate request.getCookies() (may be null) and find REMEMBER_USERNAME_COOKIE;
        //       a value that fails ValidationUtil.isValidUsername (HL-08) is ignored (KT-04: <script>alert(1)</script>).
        //       The JSP still escapes the value with c:out (CL-04).
        // Output: the username; null if there is no cookie or the value is invalid.
        return null;
    }
}
