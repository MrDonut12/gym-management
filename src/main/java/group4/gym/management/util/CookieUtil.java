package group4.gym.management.util;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Remember-username cookie (CN-37, QT-29).
 * Contains only the URL-encoded username; never a password, session ID or role.
 * The backend never uses this cookie for authentication/authorization; the cookie does not log the user in.
 */
public final class CookieUtil {

    /** Cookie name. */
    public static final String REMEMBER_USERNAME_COOKIE = "gym_username";

    /** 30-day lifetime (seconds). */
    public static final int REMEMBER_USERNAME_MAX_AGE = 30 * 24 * 60 * 60;

    private CookieUtil() {
    }

    /**
     * Send the gym_username cookie after a successful login when "Remember username" is checked.
     */
    public static void addRememberUsernameCookie(HttpServletRequest request, HttpServletResponse response, String username) {
        // TODO: Create Cookie(REMEMBER_USERNAME_COOKIE, URLEncoder.encode(username, UTF-8)); MaxAge 30 days;
        //       Path = request.getContextPath(); HttpOnly = true; SameSite = Lax (cookie.setAttribute);
        //       Secure = request.isSecure(); response.addCookie(...).
        // Output: nothing returned; the response carries a Set-Cookie header with the correct attributes.
    }

    /**
     * Delete the gym_username cookie after a successful login with the box unchecked (Max-Age=0).
     * Must not be called on a failed login or on logout.
     */
    public static void clearRememberUsernameCookie(HttpServletRequest request, HttpServletResponse response) {
        // TODO: Create a cookie with the same name/Path, empty value, MaxAge = 0, HttpOnly, SameSite=Lax; response.addCookie(...).
        // Output: nothing returned; the browser removes the cookie.
    }

    /**
     * Read the remembered username to pre-fill the login page.
     */
    public static String getRememberedUsername(HttpServletRequest request) {
        // TODO: Iterate request.getCookies(), find REMEMBER_USERNAME_COOKIE and URLDecoder.decode its value.
        // Output: the decoded username; null if there is no cookie.
        return null;
    }
}
