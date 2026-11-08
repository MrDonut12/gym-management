package group4.gym.management.util;

/**
 * Application-wide constants.
 */
public final class AppConstants {

    /** HttpSession key holding the SessionUser (id, username, fullName, role). Never holds the password. */
    public static final String SESSION_USER = "sessionUser";

    /** Session inactivity timeout: 30 minutes. Keep in sync with web.xml. */
    public static final int SESSION_TIMEOUT_MINUTES = 30;

    /** Rows per page for paginated lists. */
    public static final int PAGE_SIZE = 20;

    /** A member is "expiring soon" when the largest PAID end_date is 0..6 days after today. */
    public static final int EXPIRING_MAX_DAYS_AHEAD = 6;

    /** Root folder of the JSP views. */
    public static final String VIEW_ROOT = "/WEB-INF/";

    /** Request attribute: general error message. */
    public static final String ATTR_ERROR = "error";

    /** Request attribute: Map of errors per field. */
    public static final String ATTR_FIELD_ERRORS = "fieldErrors";

    /** Request attribute: success message. */
    public static final String ATTR_MESSAGE = "message";

    /** Session attribute: flash success message shown once after Post/Redirect/Get. */
    public static final String FLASH_MESSAGE = "flashMessage";

    /** Session attribute: flash error message shown once after Post/Redirect/Get. */
    public static final String FLASH_ERROR = "flashError";

    /** One generic message for every failed login (never says whether the username or the password is wrong). */
    public static final String MSG_LOGIN_FAILED = "Invalid username or password.";

    /** Message when a conditional status update affects 0 rows. */
    public static final String MSG_REGISTRATION_NOT_PENDING = "This registration is no longer awaiting payment.";

    /** Cancel reason stored on the PENDING registrations of a soft-deleted member (matches the seed data). */
    public static final String CANCEL_REASON_MEMBER_DELETED = "Member profile deleted";

    private AppConstants() {
    }
}
