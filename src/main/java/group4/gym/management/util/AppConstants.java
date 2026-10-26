package group4.gym.management.util;

/**
 * Application-wide constants.
 */
public final class AppConstants {

    /** HttpSession key holding the SessionUser (id, username, fullName, role; SRS 9.1). Never holds the password. */
    public static final String SESSION_USER = "sessionUser";

    /** Session inactivity timeout (CN-03, CL-05): 30 minutes. Keep in sync with web.xml. */
    public static final int SESSION_TIMEOUT_MINUTES = 30;

    /** Rows per page for paginated lists (CN-11, CN-24). */
    public static final int PAGE_SIZE = 20;

    /** QT-08: a member is "expiring soon" when the largest PAID end_date is 0..6 days after today. */
    public static final int EXPIRING_MAX_DAYS_AHEAD = 6;

    /** Root folder of the JSP views. */
    public static final String VIEW_ROOT = "/WEB-INF/";

    /** Request attribute: general error message. */
    public static final String ATTR_ERROR = "error";

    /** Request attribute: Map of errors per field. */
    public static final String ATTR_FIELD_ERRORS = "fieldErrors";

    /** Request attribute: success message. */
    public static final String ATTR_MESSAGE = "message";

    /** Session attribute: flash success message shown once after Post/Redirect/Get (CL-07). */
    public static final String FLASH_MESSAGE = "flashMessage";

    /** Session attribute: flash error message shown once after Post/Redirect/Get (CL-07). */
    public static final String FLASH_ERROR = "flashError";

    /** CN-01: one generic message for every failed login (never says whether the username or the password is wrong). */
    public static final String MSG_LOGIN_FAILED = "Invalid username or password.";

    /** QT-02: message when a conditional status update affects 0 rows. */
    public static final String MSG_REGISTRATION_NOT_PENDING = "Đăng ký không còn chờ thanh toán";

    /** QT-09: cancel reason stored on the PENDING registrations of a soft-deleted member. */
    public static final String CANCEL_REASON_MEMBER_DELETED = "Xóa hồ sơ hội viên";

    private AppConstants() {
    }
}
