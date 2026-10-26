package group4.gym.management.util;

import java.time.LocalDate;

/**
 * Field-level backend validation according to section 6 (shared utility, owner TV3). The backend never trusts the client.
 * Each service builds its own Map of field errors from these checks; duplicate checks (phone, username,
 * package name) need database queries and are therefore done in the service layer.
 * Full name, email, package name and cancel reason are trimmed BEFORE calling these methods; username and password are not.
 */
public final class ValidationUtil {

    private ValidationUtil() {
    }

    /**
     * Null or whitespace-only string.
     */
    public static boolean isBlank(String value) {
        // TODO: Check null or value.trim().isEmpty().
        // Output: true if blank.
        return false;
    }

    /**
     * HL-01 full name (member, staff): 2-100 characters after trim, no digits.
     */
    public static boolean isValidFullName(String fullName) {
        // TODO: Not null; length within 2..100; does not contain any character 0-9.
        // Output: true if valid.
        return false;
    }

    /**
     * HL-02 / HL-03 phone (member and staff use the same format): regex ^0[0-9]{9}$.
     */
    public static boolean isValidPhone(String phone) {
        // TODO: Check the regex ^0[0-9]{9}$.
        // Output: true if valid.
        return false;
    }

    /**
     * HL-04 email (optional): at most 254 characters, regex ^[^@\s]+@[^@\s]+\.[^@\s]+$.
     */
    public static boolean isValidEmail(String email) {
        // TODO: Check the length and the regex; call only when an email was entered.
        // Output: true if valid.
        return false;
    }

    /**
     * HL-05 date of birth (optional): from 01/01/1900 to today (AppClock.today()).
     */
    public static boolean isValidDateOfBirth(LocalDate dateOfBirth) {
        // TODO: 1900-01-01 <= dateOfBirth <= AppClock.today().
        // Output: true if valid.
        return false;
    }

    /**
     * HL-07 search keyword (optional): at most 100 characters.
     */
    public static boolean isValidKeyword(String keyword) {
        // TODO: null or length <= 100.
        // Output: true if valid.
        return false;
    }

    /**
     * HL-08 username: regex ^[a-zA-Z][a-zA-Z0-9_]{3,49}$ (4-50 characters, no accents, no spaces).
     */
    public static boolean isValidUsername(String username) {
        // TODO: Check the regex (the value is not trimmed).
        // Output: true if valid.
        return false;
    }

    /**
     * HL-09 password: 8-64 characters, kept exactly as entered (leading/trailing spaces count).
     */
    public static boolean isValidPassword(String password) {
        // TODO: Not null; length within 8..64.
        // Output: true if valid.
        return false;
    }

    /**
     * HL-11 package name: 2-100 characters after trim.
     */
    public static boolean isValidPackageName(String name) {
        // TODO: Not null; length within 2..100.
        // Output: true if valid.
        return false;
    }

    /**
     * HL-11 normalize a package name for duplicate checks: collapse runs of whitespace, trim, lower-case.
     */
    public static String normalizePackageName(String name) {
        // TODO: name.trim().replaceAll("\\s+", " ").toLowerCase().
        // Output: the normalized string; null if name is null.
        return null;
    }

    /**
     * HL-12 package price: digits only, integer 1..100,000,000 VND.
     */
    public static Integer parsePrice(String raw) {
        // TODO: raw matches ^[0-9]+$ (no sign, no decimal separator) and parses to a value within 1..100000000.
        // Output: the price; null if invalid.
        return null;
    }

    /**
     * HL-13 package duration: 1, 3, 6 or 12 months.
     */
    public static boolean isValidDurationMonths(Integer months) {
        // TODO: months is one of 1, 3, 6, 12.
        // Output: true if valid.
        return false;
    }

    /**
     * HL-15 cancel reason: 3-255 characters after trim.
     */
    public static boolean isValidCancelReason(String reason) {
        // TODO: Not null; length within 3..255.
        // Output: true if valid.
        return false;
    }
}
