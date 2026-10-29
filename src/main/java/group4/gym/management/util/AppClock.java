
package group4.gym.management.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;

/**
 * Single business clock (QT-15, SRS 9.1).
 */
public final class AppClock {

    public static final ZoneId ZONE =
            ZoneId.of("Asia/Ho_Chi_Minh");

    private AppClock() {
    }

    /**
     * Current business date-time in Vietnam.
     * Precision: seconds (DATETIME2(0)).
     */
    public static LocalDateTime now() {
        return LocalDateTime.now(ZONE)
                .truncatedTo(ChronoUnit.SECONDS);
    }

    /**
     * Current business date in Vietnam.
     */
    public static LocalDate today() {
        return LocalDate.now(ZONE);
    }
}
