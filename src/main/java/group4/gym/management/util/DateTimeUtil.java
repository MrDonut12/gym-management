package group4.gym.management.util;

import group4.gym.management.model.enums.DurationUnit;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;

/**
 * Business date/time helpers. All business dates and times use Vietnam time (section 1.3).
 */
public final class DateTimeUtil {

    /** Business time zone. */
    public static final ZoneId VN_ZONE = ZoneId.of("Asia/Ho_Chi_Minh");

    /** Opening time: check-in is allowed from 07:00 (CN-13). */
    public static final LocalTime GYM_OPEN_TIME = LocalTime.of(7, 0);

    /** Cut-off for check-in and for auto-closing OPEN visits: 20:00 (CN-13, CN-15). */
    public static final LocalTime CHECK_IN_CUTOFF = LocalTime.of(20, 0);

    /** Number of calendar days for the expiry warning (section 3.1.2). */
    public static final int EXPIRY_WARNING_DAYS = 7;

    /** Date pattern for display/input. */
    public static final String DATE_PATTERN = "dd/MM/yyyy";

    /** Date-time pattern for display. */
    public static final String DATE_TIME_PATTERN = "dd/MM/yyyy HH:mm:ss";

    private DateTimeUtil() {
    }

    /**
     * Current date and time in Vietnam time.
     */
    public static LocalDateTime now() {
        // TODO: Get the current date-time using VN_ZONE.
        // Output: the current LocalDateTime in Vietnam time.
        return null;
    }

    /**
     * 00:00 today in Vietnam time (start date of a new chain when confirming PAID, QT-25).
     */
    public static LocalDateTime todayStart() {
        // TODO: Take today's date in VN_ZONE and combine it with 00:00.
        // Output: LocalDateTime 00:00:00 of today.
        return null;
    }

    /**
     * 00:00 of the given date.
     */
    public static LocalDateTime startOfDay(LocalDate date) {
        // TODO: Combine date with 00:00.
        // Output: LocalDateTime 00:00:00 of date.
        return null;
    }

    /**
     * The 20:00 boundary of the check-in date (end time of an AUTO_CLOSED visit, CN-15).
     */
    public static LocalDateTime cutoffOf(LocalDate checkInDate) {
        // TODO: Combine checkInDate with CHECK_IN_CUTOFF.
        // Output: LocalDateTime 20:00:00 of checkInDate.
        return null;
    }

    /**
     * Whether the moment is within check-in hours: from 07:00 up to (but excluding) 20:00 (CN-13).
     */
    public static boolean isWithinCheckInHours(LocalDateTime dateTime) {
        // TODO: Compare the time part of dateTime with GYM_OPEN_TIME (inclusive) and CHECK_IN_CUTOFF (exclusive).
        // Output: true if 07:00 <= time < 20:00, otherwise false.
        return false;
    }

    /**
     * Add a plan duration using calendar rules (rolling calendar, QT-19).
     * Months/years are added by calendar, not converted to 30/365 days; if the target month lacks the day, use its last day.
     */
    public static LocalDateTime addDuration(LocalDateTime startAt, int durationValue, DurationUnit unit) {
        // TODO: Add durationValue by unit (DAY -> plusDays, MONTH -> plusMonths, YEAR -> plusYears).
        // Output: end_exclusive (LocalDateTime) greater than startAt. Example: 31/01/2027 + 1 month = 28/02/2027.
        return null;
    }

    /**
     * Last training day = the day before end_exclusive (section 3.1.3).
     */
    public static LocalDate lastTrainingDate(LocalDateTime endExclusive) {
        // TODO: Take the date of endExclusive minus 1 day (endExclusive is always 00:00).
        // Output: LocalDate of the last training day; null if endExclusive is null.
        return null;
    }

    /**
     * Start boundary of a filter range: 00:00:00 on the start date (section 3.1.6).
     */
    public static LocalDateTime rangeStart(LocalDate fromDate) {
        // TODO: Return 00:00 of fromDate.
        // Output: inclusive LocalDateTime; null if fromDate is null.
        return null;
    }

    /**
     * Exclusive end boundary of a filter range: 00:00:00 on the day after the end date (section 3.1.6).
     */
    public static LocalDateTime rangeEndExclusive(LocalDate toDate) {
        // TODO: Return 00:00 of (toDate + 1 day).
        // Output: exclusive LocalDateTime; null if toDate is null.
        return null;
    }

    /**
     * Parse a date string coming from a form (dd/MM/yyyy or the yyyy-MM-dd of input type="date").
     */
    public static LocalDate parseDate(String value) {
        // TODO: Parse value using the supported formats; an empty string returns null.
        // Output: LocalDate; throw ValidationException or return null on a bad format (the team must pick one behavior).
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
