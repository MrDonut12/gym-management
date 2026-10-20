package group4.gym.management.util;

import group4.gym.management.model.Account;
import group4.gym.management.model.Member;
import group4.gym.management.model.PlanType;
import java.time.LocalDate;
import java.util.Map;

/**
 * Backend validation according to section 3.4 (CN-30). The backend never trusts client-side validation.
 * The validateXxx methods return a Map of errors per field (empty = valid); duplicate checks (phone, username,
 * plan name) need database queries and are therefore done in the service layer.
 */
public final class ValidationUtil {

    private ValidationUtil() {
    }

    /**
     * Validate a member profile: full name, phone, email, date of birth, gender.
     */
    public static Map<String, String> validateMember(Member member) {
        // TODO: Call isValidFullName, isValidMemberPhone, isValidEmail (if provided), isNotFutureDate (if provided).
        // Output: Map<field name, error message>; an empty Map when valid.
        return null;
    }

    /**
     * Validate staff account data (DL-07): username, full name, phone, role, email.
     *
     * @param rawPassword the password when creating; null when updating
     * @param isCreate    true for a create operation
     */
    public static Map<String, String> validateAccount(Account account, String rawPassword, boolean isCreate) {
        // TODO: Check required fields, isValidStaffPhone, isValidEmail (if provided), role not null; check the password on create.
        // Output: Map<field name, error message>; an empty Map when valid.
        return null;
    }

    /**
     * Validate a plan type: name, positive integer price, positive duration within the fixed catalog.
     */
    public static Map<String, String> validatePlanType(PlanType planType) {
        // TODO: Check name not blank, price > 0, durationValue > 0, (durationValue, durationUnit) in 1 day/1 month/6 months/1 year.
        // Output: Map<field name, error message>; an empty Map when valid.
        return null;
    }

    /**
     * Validate a new password and its confirmation (CN-36).
     */
    public static Map<String, String> validateNewPassword(String newPassword, String confirmPassword) {
        // TODO: Check not blank, meets the minimum length agreed by the team, and equals confirmPassword.
        // Output: Map<field name, error message>; an empty Map when valid.
        return null;
    }

    /**
     * Full name: 2-100 characters, not only whitespace.
     */
    public static boolean isValidFullName(String fullName) {
        // TODO: Trim, then check the length is within 2..100.
        // Output: true if valid.
        return false;
    }

    /**
     * Member phone: exactly 10 digits, starting with 0; carrier prefixes are not checked.
     */
    public static boolean isValidMemberPhone(String phone) {
        // TODO: Check the regex ^0\d{9}$.
        // Output: true if valid.
        return false;
    }

    /**
     * Staff phone: exactly 10 digits (not required to start with 0).
     */
    public static boolean isValidStaffPhone(String phone) {
        // TODO: Check the regex ^\d{10}$.
        // Output: true if valid.
        return false;
    }

    /**
     * Email: valid format, at most 254 characters (only checked when provided).
     */
    public static boolean isValidEmail(String email) {
        // TODO: Check length <= 254 and the email format.
        // Output: true if valid.
        return false;
    }

    /**
     * Date not after the current date (Vietnam time).
     */
    public static boolean isNotFutureDate(LocalDate date) {
        // TODO: Compare date with today's date in DateTimeUtil.VN_ZONE.
        // Output: true if date <= today.
        return false;
    }

    /**
     * Normalize a plan name for duplicate checks: collapse whitespace, trim, lower-case.
     */
    public static String normalizePlanName(String name) {
        // TODO: Replace runs of whitespace with a single space, trim, toLowerCase.
        // Output: the normalized string; null if name is null.
        return null;
    }

    /**
     * Null or whitespace-only string.
     */
    public static boolean isBlank(String value) {
        // TODO: Check null or value.trim().isEmpty().
        // Output: true if blank.
        return false;
    }
}
