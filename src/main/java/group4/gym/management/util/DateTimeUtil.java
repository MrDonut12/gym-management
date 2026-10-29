
package group4.gym.management.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Parsing and formatting of dates for forms and views.
 * "Now"/"today" always come from AppClock (QT-15).
 */
public final class DateTimeUtil {

    /** Date pattern for display. */
    public static final String DATE_PATTERN = "dd/MM/yyyy";

    /** Date-time pattern for display. */
    public static final String DATE_TIME_PATTERN = "dd/MM/yyyy HH:mm";

    private static final DateTimeFormatter DATE_FORMATTER =
            DateTimeFormatter.ofPattern(DATE_PATTERN);

    private static final DateTimeFormatter DATE_TIME_FORMATTER =
            DateTimeFormatter.ofPattern(DATE_TIME_PATTERN);

    private DateTimeUtil() {
    }

    /**
     * Parse date from HTML input type="date".
     * Expected format: yyyy-MM-dd.
     */
    public static LocalDate parseDate(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }

        return LocalDate.parse(value.trim());
    }

    /**
     * Format a date for display.
     */
    public static String format(LocalDate date) {
        if (date == null) {
            return "";
        }

        return date.format(DATE_FORMATTER);
    }

    /**
     * Format a date-time for display.
     */
    public static String format(LocalDateTime dateTime) {
        if (dateTime == null) {
            return "";
        }

        return dateTime.format(DATE_TIME_FORMATTER);
    }
}
