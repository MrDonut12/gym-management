package group4.gym.management.util;

import group4.gym.management.model.Account;
import group4.gym.management.model.Member;
import group4.gym.management.model.PlanType;
import group4.gym.management.model.enums.DurationUnit;
import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Backend validation according to section 3.4 (CN-30). The backend never trusts client-side validation.
 * The validateXxx methods return a Map of errors per field (empty = valid); duplicate checks (phone, username,
 * plan name) need database queries and are therefore done in the service layer.
 * Keep every rule identical to assets/js/validation.js.
 */
public final class ValidationUtil {

    /** Minimum password length (not specified in the SRS; agreed proposal: 8 characters). Mirrored in validation.js. */
    public static final int MIN_PASSWORD_LENGTH = 8;

    private static final int FULL_NAME_MIN = 2;
    private static final int FULL_NAME_MAX = 100;
    private static final int USERNAME_MAX = 50;
    private static final int PLAN_NAME_MAX = 100;
    private static final int EMAIL_MAX = 254;

    private static final Pattern MEMBER_PHONE = Pattern.compile("^0\\d{9}$");
    private static final Pattern STAFF_PHONE = Pattern.compile("^\\d{10}$");
    private static final Pattern EMAIL = Pattern.compile("^[A-Za-z0-9._%+-]+@[A-Za-z0-9-]+(?:\\.[A-Za-z0-9-]+)*\\.[A-Za-z]{2,}$");
    private static final Pattern WHITESPACE_RUN = Pattern.compile("\\s+");

    private ValidationUtil() {
    }

    /**
     * Validate a member profile: full name, phone, email, date of birth, gender.
     */
    public static Map<String, String> validateMember(Member member) {
        Map<String, String> errors = new LinkedHashMap<>();
        if (member == null) {
            errors.put("member", "Member data is required.");
            return errors;
        }
        if (!isValidFullName(member.getFullName())) {
            errors.put("fullName", "Full name must be 2-100 characters.");
        }
        if (isBlank(member.getPhone())) {
            errors.put("phone", "Phone is required.");
        } else if (!isValidMemberPhone(member.getPhone())) {
            errors.put("phone", "Phone must be exactly 10 digits and start with 0.");
        }
        if (!isBlank(member.getEmail()) && !isValidEmail(member.getEmail())) {
            errors.put("email", "Email is not in a valid format (at most 254 characters).");
        }
        if (member.getDateOfBirth() != null && !isNotFutureDate(member.getDateOfBirth())) {
            errors.put("dateOfBirth", "Date of birth must not be after today.");
        }
        // Gender is optional and already restricted to the Gender enum by the time it reaches the model.
        return errors;
    }

    /**
     * Validate staff account data (DL-07): username, full name, phone, role, email.
     *
     * @param rawPassword the password when creating; null when updating
     * @param isCreate    true for a create operation
     */
    public static Map<String, String> validateAccount(Account account, String rawPassword, boolean isCreate) {
        Map<String, String> errors = new LinkedHashMap<>();
        if (account == null) {
            errors.put("account", "Account data is required.");
            return errors;
        }
        if (isCreate) {
            String username = account.getUsername();
            if (isBlank(username)) {
                errors.put("username", "Username is required.");
            } else if (username.trim().length() > USERNAME_MAX) {
                errors.put("username", "Username must be at most " + USERNAME_MAX + " characters.");
            }
            if (isBlank(rawPassword)) {
                errors.put("password", "Password is required.");
            } else if (rawPassword.length() < MIN_PASSWORD_LENGTH) {
                errors.put("password", "Password must be at least " + MIN_PASSWORD_LENGTH + " characters.");
            }
        }
        if (!isValidFullName(account.getFullName())) {
            errors.put("fullName", "Full name must be 2-100 characters.");
        }
        if (isBlank(account.getPhone())) {
            errors.put("phone", "Phone is required.");
        } else if (!isValidStaffPhone(account.getPhone())) {
            errors.put("phone", "Phone must be exactly 10 digits.");
        }
        if (!isBlank(account.getEmail()) && !isValidEmail(account.getEmail())) {
            errors.put("email", "Email is not in a valid format (at most 254 characters).");
        }
        if (account.getRole() == null) {
            errors.put("role", "Role is required.");
        }
        return errors;
    }

    /**
     * Validate a plan type: name, positive integer price, positive duration within the fixed catalog.
     */
    public static Map<String, String> validatePlanType(PlanType planType) {
        Map<String, String> errors = new LinkedHashMap<>();
        if (planType == null) {
            errors.put("planType", "Plan type data is required.");
            return errors;
        }
        if (isBlank(planType.getName())) {
            errors.put("name", "Plan name is required.");
        } else if (planType.getName().trim().length() > PLAN_NAME_MAX) {
            errors.put("name", "Plan name must be at most " + PLAN_NAME_MAX + " characters.");
        }
        if (planType.getPrice() <= 0) {
            errors.put("price", "Price must be an integer greater than 0.");
        }
        if (planType.getDurationValue() <= 0 || planType.getDurationUnit() == null) {
            errors.put("duration", "Duration is required.");
        } else if (!isInDurationCatalog(planType.getDurationValue(), planType.getDurationUnit())) {
            errors.put("duration", "Duration must be one of: 1 day, 1 month, 6 months, 1 year.");
        }
        return errors;
    }

    /**
     * Validate a new password and its confirmation (CN-36).
     */
    public static Map<String, String> validateNewPassword(String newPassword, String confirmPassword) {
        Map<String, String> errors = new LinkedHashMap<>();
        if (isBlank(newPassword)) {
            errors.put("newPassword", "New password is required.");
        } else if (newPassword.length() < MIN_PASSWORD_LENGTH) {
            errors.put("newPassword", "New password must be at least " + MIN_PASSWORD_LENGTH + " characters.");
        }
        if (isBlank(confirmPassword)) {
            errors.put("confirmPassword", "Please confirm the new password.");
        } else if (!confirmPassword.equals(newPassword)) {
            errors.put("confirmPassword", "Confirmation does not match the new password.");
        }
        return errors;
    }

    /**
     * Full name: 2-100 characters, not only whitespace.
     */
    public static boolean isValidFullName(String fullName) {
        if (fullName == null) {
            return false;
        }
        int length = fullName.trim().length();
        return length >= FULL_NAME_MIN && length <= FULL_NAME_MAX;
    }

    /**
     * Member phone: exactly 10 digits, starting with 0; carrier prefixes are not checked.
     */
    public static boolean isValidMemberPhone(String phone) {
        return phone != null && MEMBER_PHONE.matcher(phone).matches();
    }

    /**
     * Staff phone: exactly 10 digits (not required to start with 0).
     */
    public static boolean isValidStaffPhone(String phone) {
        return phone != null && STAFF_PHONE.matcher(phone).matches();
    }

    /**
     * Email: valid format, at most 254 characters (only checked when provided).
     */
    public static boolean isValidEmail(String email) {
        if (email == null) {
            return false;
        }
        String trimmed = email.trim();
        return trimmed.length() <= EMAIL_MAX && EMAIL.matcher(trimmed).matches();
    }

    /**
     * Date not after the current date (Vietnam time).
     */
    public static boolean isNotFutureDate(LocalDate date) {
        return date != null && !date.isAfter(LocalDate.now(DateTimeUtil.VN_ZONE));
    }

    /**
     * Normalize a plan name for duplicate checks: collapse whitespace, trim, lower-case.
     */
    public static String normalizePlanName(String name) {
        if (name == null) {
            return null;
        }
        return WHITESPACE_RUN.matcher(name).replaceAll(" ").trim().toLowerCase(Locale.ROOT);
    }

    /**
     * Null or whitespace-only string.
     */
    public static boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }

    /** Fixed catalog (DL-02): 1 day / 1 month / 6 months / 1 year. */
    private static boolean isInDurationCatalog(int value, DurationUnit unit) {
        return (value == 1 && unit == DurationUnit.DAY)
                || (value == 1 && unit == DurationUnit.MONTH)
                || (value == 6 && unit == DurationUnit.MONTH)
                || (value == 1 && unit == DurationUnit.YEAR);
    }
}
