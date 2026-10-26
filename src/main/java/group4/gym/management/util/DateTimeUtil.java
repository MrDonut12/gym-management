package group4.gym.management.util;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Parsing and formatting of dates for forms and views. "Now"/"today" always come from AppClock (QT-15).
 */
public final class DateTimeUtil {

    /** Date pattern for display. */
    public static final String DATE_PATTERN = "dd/MM/yyyy";

    /** Date-time pattern for display. */
    public static final String DATE_TIME_PATTERN = "dd/MM/yyyy HH:mm";

    private DateTimeUtil() {
    }

    /**
     * Parse a date string coming from a form (the yyyy-MM-dd of input type="date").
     */
    public static LocalDate parseDate(String value) {
        // TODO: Blank -> null; otherwise LocalDate.parse(value.trim()).
        //       An invalid date lets DateTimeParseException propagate: the caller turns it into a field error (HL-05, HL-16).
        // Output: LocalDate or null.
        return null;
    }

    /**
     * Format a date for display.
     */
    public static String format(LocalDate date) {
        // TODO: Format using DATE_PATTERN.
        // Output: display string; empty string if date is null.
        return null;
    }

    /**
     * Format a date-time for display.
     */
    public static String format(LocalDateTime dateTime) {
        // TODO: Format using DATE_TIME_PATTERN.
        // Output: display string; empty string if dateTime is null.
        return null;
    }
}
