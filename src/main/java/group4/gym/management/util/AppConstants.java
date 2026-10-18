package group4.gym.management.util;

/**
 * Application-wide constants.
 */
public final class AppConstants {

    /** HttpSession key holding the logged-in account ID (only the ID is stored, never the password). */
    public static final String SESSION_ACCOUNT_ID = "accountId";

    /** Request attribute holding the current Account (reloaded on every request by AuthenticationFilter). */
    public static final String REQUEST_CURRENT_ACCOUNT = "currentAccount";

    /** Session inactivity timeout (CN-03): 60 minutes. Keep in sync with web.xml. */
    public static final int SESSION_TIMEOUT_MINUTES = 60;

    /** Root folder of the JSP views. */
    public static final String VIEW_ROOT = "/WEB-INF/views/";

    /** Request attribute: general error message. */
    public static final String ATTR_ERROR = "error";

    /** Request attribute: Map of errors per field. */
    public static final String ATTR_FIELD_ERRORS = "fieldErrors";

    /** Request/session attribute: success message. */
    public static final String ATTR_MESSAGE = "message";

    /** Display name used when an action is performed by the System (lazy close of visits, CN-15). */
    public static final String SYSTEM_ACTOR_NAME = "System";

    private AppConstants() {
    }
}
