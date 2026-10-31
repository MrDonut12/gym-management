package group4.gym.management.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;

/**
 * The single business clock (QT-15, shared interface SRS 9.1, owner TV4).
 * Every created_at, paid_at, cancelled_at, "today" and report period comes from here.
 * Never use SQL Server GETDATE()/SYSDATETIME() or the OS default time zone for business calculations.
 */
public final class AppClock {

    /** Business time zone (SRS 1.4). */
    public static final ZoneId ZONE = ZoneId.of("Asia/Ho_Chi_Minh");

    private AppClock() {
    }

    /**
     * Current business date-time in Vietnam time.
     */
    public static LocalDateTime now() {
        return LocalDateTime.now(ZONE).truncatedTo(ChronoUnit.SECONDS);
    }

    /**
     * "Today" in Vietnam time (SRS 1.5), used by QT-05, QT-07, QT-08, QT-09 and HL-05.
     */
    public static LocalDate today() {
        return LocalDate.now(ZONE);
    }
}