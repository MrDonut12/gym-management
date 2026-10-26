package group4.gym.management.util;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Date filter range (QT-13, HL-16; shared utility, owner TV4).
 * "From A to B" means [A 00:00, B+1 00:00): both whole days are included. A <= B. Default: the current month.
 * Used by the registration list (CN-24) and the reports (CN-26 to CN-28).
 */
public final class DateRange {

    /** First day (inclusive) */
    private final LocalDate from;

    /** Last day (inclusive of the whole day) */
    private final LocalDate to;

    private DateRange(LocalDate from, LocalDate to) {
        this.from = from;
        this.to = to;
    }

    /**
     * Build a range from the filter inputs.
     *
     * @param from null = not provided
     * @param to   null = not provided
     */
    public static DateRange of(LocalDate from, LocalDate to) {
        // TODO: A missing bound takes the matching bound of currentMonth() (QT-13 default);
        //       from > to -> throw ValidationException with a field error on "toDate" (HL-16).
        // Output: a valid DateRange.
        return null;
    }

    /**
     * Default range: first to last day of the current month (by AppClock.today()).
     */
    public static DateRange currentMonth() {
        // TODO: today = AppClock.today(); new DateRange(today.withDayOfMonth(1), today.withDayOfMonth(today.lengthOfMonth())).
        // Output: the DateRange of the current month.
        return null;
    }

    public LocalDate getFrom() {
        return from;
    }

    public LocalDate getTo() {
        return to;
    }

    /**
     * SQL lower bound: column >= getFromInclusive().
     */
    public LocalDateTime getFromInclusive() {
        // TODO: from.atStartOfDay().
        // Output: 00:00 of the first day.
        return null;
    }

    /**
     * SQL upper bound: column < getToExclusive() (KT-19: 31/10 23:30 is included, 01/11 00:00 is not).
     */
    public LocalDateTime getToExclusive() {
        // TODO: to.plusDays(1).atStartOfDay().
        // Output: 00:00 of the day after the last day.
        return null;
    }
}
