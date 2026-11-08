package group4.gym.management.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;

/**
 * The single business clock.
 * Every created_at, paid_at, cancelled_at, "today" and report period comes from here.
 * Never use SQL Server GETDATE()/SYSDATETIME() or the OS default time zone for business calculations.
 */
public final class AppClock {

    /** Business time zone. */
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
     * "Today" in Vietnam time.
     */
    public static LocalDate today() {
        return LocalDate.now(ZONE);
    }
}