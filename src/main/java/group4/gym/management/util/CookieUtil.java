package group4.gym.management.util;

import jakarta.servlet.http.Cookie;
import java.net.URLEncoder;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

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
        writeCookie(request, response, URLEncoder.encode(username, StandardCharsets.UTF_8), REMEMBER_USERNAME_MAX_AGE);
    }

    /**
     * Delete the gym_username cookie after a successful login with the box unchecked (Max-Age=0).
     * Must not be called on a failed login or on logout.
     */
    public static void clearRememberUsernameCookie(HttpServletRequest request, HttpServletResponse response) {
        writeCookie(request, response, "", 0);
    }

    /**
     * Read the remembered username to pre-fill the login page.
     */
    public static String getRememberedUsername(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (REMEMBER_USERNAME_COOKIE.equals(cookie.getName())) {
                    try {
                        return URLDecoder.decode(cookie.getValue(), StandardCharsets.UTF_8);
                    } catch (IllegalArgumentException ex) {
                        return null;
                    }
                }
            }
        }
        return null;
    }
    private static void writeCookie(HttpServletRequest request, HttpServletResponse response, String value, int maxAge) {
        String path = request.getContextPath().isEmpty() ? "/" : request.getContextPath();
        response.addHeader("Set-Cookie", REMEMBER_USERNAME_COOKIE + "=" + value
                + "; Max-Age=" + maxAge + "; Path=" + path + "; HttpOnly; SameSite=Lax"
                + (request.isSecure() ? "; Secure" : ""));
    }
}
