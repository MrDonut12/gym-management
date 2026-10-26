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
     * Read a trimmed string parameter (full name, email, package name, cancel reason, keyword; section 6).
     */
    public static String getString(HttpServletRequest request, String name) {
        // TODO: request.getParameter(name), trim; an empty string -> null.
        // Output: the trimmed string or null.
        return null;
    }

    /**
     * Read a string parameter exactly as sent: username and password are NOT trimmed (section 6, HL-09).
     */
    public static String getRawString(HttpServletRequest request, String name) {
        // TODO: request.getParameter(name) unchanged.
        // Output: the raw string or null.
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
     * Read an ID from the URL/form (HL-17).
     */
    public static Integer getPositiveId(HttpServletRequest request, String name) {
        // TODO: getInteger; value <= 0 -> null. The servlet answers 404 when the result is null.
        // Output: a positive Integer or null.
        return null;
    }

    /**
     * Read the page number (HL-07).
     */
    public static int getPage(HttpServletRequest request) {
        // TODO: getInteger(request, "page"); null or < 1 -> 1.
        // Output: a page number >= 1.
        return 1;
    }

    /**
     * Read a date parameter.
     */
    public static LocalDate getDate(HttpServletRequest request, String name) {
        // TODO: DateTimeUtil.parseDate(request.getParameter(name)); an invalid value propagates DateTimeParseException.
        // Output: LocalDate or null when blank.
        return null;
    }

    /**
     * Read an enum parameter by constant name (role, status, payment method...; HL-10, HL-14).
     */
    public static <E extends Enum<E>> E getEnum(HttpServletRequest request, String name, Class<E> enumType) {
        // TODO: Enum.valueOf(enumType, value); an invalid or empty value -> null.
        // Output: the enum constant or null.
        return null;
    }
}
