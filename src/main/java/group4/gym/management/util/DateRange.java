
package group4.gym.management.util;

import group4.gym.management.exception.ValidationException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;

/**
 * Date filter range (QT-13, HL-16).
 *
 * From A to B means [A 00:00, B+1 00:00).
 * Both whole days are included.
 * Default: current month.
 */
public final class DateRange {

    private final LocalDate from;
    private final LocalDate to;

    private DateRange(LocalDate from, LocalDate to) {
        this.from = from;
        this.to = to;
    }

    /**
     * Build a date range from filter inputs.
     */
    public static DateRange of(LocalDate from, LocalDate to) {
        DateRange current = currentMonth();

        if (from == null) {
            from = current.getFrom();
        }

        if (to == null) {
            to = current.getTo();
        }

        if (from.isAfter(to)) {
            throw new ValidationException(
                    "Invalid date range",
                    Map.of("toDate",
                            "Ngày kết thúc phải lớn hơn hoặc bằng ngày bắt đầu.")
            );
        }

        return new DateRange(from, to);
    }

    /**
     * Current month, based on Vietnam business time.
     */
    public static DateRange currentMonth() {
        LocalDate today = AppClock.today();

        return new DateRange(
                today.withDayOfMonth(1),
                today.withDayOfMonth(today.lengthOfMonth())
        );
    }

    public LocalDate getFrom() {
        return from;
    }

    public LocalDate getTo() {
        return to;
    }

    /**
     * Inclusive lower bound.
     */
    public LocalDateTime getFromInclusive() {
        return from.atStartOfDay();
    }

    /**
     * Exclusive upper bound.
     */
    public LocalDateTime getToExclusive() {
        return to.plusDays(1).atStartOfDay();
    }
}
