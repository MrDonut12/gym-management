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
        String value = request.getParameter(name);
        if (value == null) {
            return null;
        }
        value = value.trim();
        return value.isEmpty() ? null : value;
    }

    /**
     * Read a string parameter exactly as sent: username and password are NOT trimmed (section 6, HL-09).
     */
    public static String getRawString(HttpServletRequest request, String name) {
        return request.getParameter(name);
    }

    /**
     * Read an integer parameter.
     */
    public static Integer getInteger(HttpServletRequest request, String name) {
        String value = getString(request, name);
        if (value == null) {
            return null;
        }
        try {
            return Integer.valueOf(value);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    /**
     * Read an ID from the URL/form (HL-17).
     */
    public static Integer getPositiveId(HttpServletRequest request, String name) {
        Integer id = getInteger(request, name);
        return (id == null || id <= 0) ? null : id;
    }

    /**
     * Read the page number (HL-07).
     */
    public static int getPage(HttpServletRequest request) {
        Integer page = getInteger(request, "page");
        return (page == null || page < 1) ? 1 : page;
    }

    /**
     * Read a date parameter.
     */
    public static LocalDate getDate(HttpServletRequest request, String name) {
        return DateTimeUtil.parseDate(request.getParameter(name));
    }

    /**
     * Read an enum parameter by constant name (role, status, payment method...; HL-10, HL-14).
     */
    public static <E extends Enum<E>> E getEnum(HttpServletRequest request, String name, Class<E> enumType) {
        String value = getString(request, name);
        if (value == null) {
            return null;
        }
        try {
            return Enum.valueOf(enumType, value);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}
