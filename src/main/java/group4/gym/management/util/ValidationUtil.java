package group4.gym.management.util;

import java.time.LocalDate;
import java.util.regex.Pattern;

/**
 * Field-level backend validation according to section 6 (shared utility, owner TV3). The backend never trusts the client.
 * Each service builds its own Map of field errors from these checks; duplicate checks (phone, username,
 * package name) need database queries and are therefore done in the service layer.
 * Full name, email, package name and cancel reason are trimmed BEFORE calling these methods; username and password are not.
 */
public final class ValidationUtil {

    private static final Pattern PHONE = Pattern.compile("0[0-9]{9}");
    private static final Pattern EMAIL = Pattern.compile("[^@\\s]+@[^@\\s]+\\.[^@\\s]+");
    private static final Pattern USERNAME = Pattern.compile("[a-zA-Z][a-zA-Z0-9_]{3,49}");
    private static final Pattern DIGITS = Pattern.compile("[0-9]+");
    private static final LocalDate MIN_DATE_OF_BIRTH = LocalDate.of(1900, 1, 1);
    private static final int MAX_PRICE = 100_000_000;

    private ValidationUtil() {
    }

    /**
     * Null or whitespace-only string.
     */
    public static boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }

    /**
     * HL-01 full name (member, staff): 2-100 characters after trim, no digits.
     */
    public static boolean isValidFullName(String fullName) {
        if (fullName == null || fullName.length() < 2 || fullName.length() > 100) {
            return false;
        }
        for (int i = 0; i < fullName.length(); i++) {
            char c = fullName.charAt(i);
            if (c >= '0' && c <= '9') {
                return false;
            }
        }
        return true;
    }

    /**
     * HL-02 / HL-03 phone (member and staff use the same format): regex ^0[0-9]{9}$.
     */
    public static boolean isValidPhone(String phone) {
        return phone != null && PHONE.matcher(phone).matches();
    }

    /**
     * HL-04 email (optional): at most 254 characters, regex ^[^@\s]+@[^@\s]+\.[^@\s]+$.
     */
    public static boolean isValidEmail(String email) {
        return email != null && email.length() <= 254 && EMAIL.matcher(email).matches();
    }

    /**
     * HL-05 date of birth (optional): from 01/01/1900 to today (AppClock.today()).
     */
    public static boolean isValidDateOfBirth(LocalDate dateOfBirth) {
        return dateOfBirth != null
                && !dateOfBirth.isBefore(MIN_DATE_OF_BIRTH)
                && !dateOfBirth.isAfter(AppClock.today());
    }

    /**
     * HL-07 search keyword (optional): at most 100 characters.
     */
    public static boolean isValidKeyword(String keyword) {
        return keyword == null || keyword.length() <= 100;
    }

    /**
     * HL-08 username: regex ^[a-zA-Z][a-zA-Z0-9_]{3,49}$ (4-50 characters, no accents, no spaces).
     */
    public static boolean isValidUsername(String username) {
        return username != null && USERNAME.matcher(username).matches();
    }

    /**
     * HL-09 password: 8-64 characters, kept exactly as entered (leading/trailing spaces count).
     */
    public static boolean isValidPassword(String password) {
        return password != null && password.length() >= 8 && password.length() <= 64;
    }

    /**
     * HL-11 package name: 2-100 characters after trim.
     */
    public static boolean isValidPackageName(String name) {
        return name != null && name.length() >= 2 && name.length() <= 100;
    }

    /**
     * HL-11 normalize a package name for duplicate checks: collapse runs of whitespace, trim, lower-case.
     */
    public static String normalizePackageName(String name) {
        if (name == null) {
            return null;
        }
        return name.trim().replaceAll("\\s+", " ").toLowerCase();
    }

    /**
     * HL-12 package price: digits only, integer 1..100,000,000 VND.
     */
    public static Integer parsePrice(String raw) {
        // The length guard keeps parseInt away from overflow on absurdly long digit strings.
        if (raw == null || raw.length() > 18 || !DIGITS.matcher(raw).matches()) {
            return null;
        }
        long price = Long.parseLong(raw);
        return (price >= 1 && price <= MAX_PRICE) ? Integer.valueOf((int) price) : null;
    }

    /**
     * HL-13 package duration: 1, 3, 6 or 12 months.
     */
    public static boolean isValidDurationMonths(Integer months) {
        return months != null && (months == 1 || months == 3 || months == 6 || months == 12);
    }

    /**
     * HL-15 cancel reason: 3-255 characters after trim.
     */
    public static boolean isValidCancelReason(String reason) {
        return reason != null && reason.length() >= 3 && reason.length() <= 255;
    }
}
