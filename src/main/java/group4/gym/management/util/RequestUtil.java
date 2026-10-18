package group4.gym.management.util;

import jakarta.servlet.http.HttpServletRequest;
import java.time.LocalDate;

/**
 * Reads and converts request parameters. Not used for business validation.
 */
public final class RequestUtil {

    private RequestUtil() {
    }

    /**
     * Read a trimmed string parameter.
     */
    public static String getString(HttpServletRequest request, String name) {
        // TODO: request.getParameter(name), trim; an empty string -> null.
        // Output: the trimmed string or null.
        return null;
    }

    /**
     * Read an integer parameter.
     */
    public static Integer getInteger(HttpServletRequest request, String name) {
        // TODO: Parse an Integer; a bad format or empty value -> null (never leak NumberFormatException).
        // Output: Integer or null.
        return null;
    }

    /**
     * Read a long parameter (money amounts).
     */
    public static Long getLong(HttpServletRequest request, String name) {
        // TODO: Parse a Long; a bad format or empty value -> null.
        // Output: Long or null.
        return null;
    }

    /**
     * Read a date parameter.
     */
    public static LocalDate getDate(HttpServletRequest request, String name) {
        // TODO: Use DateTimeUtil.parseDate.
        // Output: LocalDate or null.
        return null;
    }

    /**
     * Read an enum parameter by constant name.
     */
    public static <E extends Enum<E>> E getEnum(HttpServletRequest request, String name, Class<E> enumType) {
        // TODO: Enum.valueOf(enumType, value); an invalid value -> null.
        // Output: the enum constant or null.
        return null;
    }

    /**
     * Whether the request is a POST (state-changing actions accept POST only).
     */
    public static boolean isPost(HttpServletRequest request) {
        // TODO: Compare request.getMethod() with "POST".
        // Output: true if POST.
        return false;
    }
}
