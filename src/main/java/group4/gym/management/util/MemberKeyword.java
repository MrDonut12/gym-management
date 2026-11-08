package group4.gym.management.util;

/**
 * Classifies the single member search box (FR-11, FR-24). The kind of the input decides the search, in this order:
 * <ol>
 *   <li>PHONE - a valid phone number (VR-02: 0 + 9 digits): exact match on phone;</li>
 *   <li>NAME  - no digit at all (VR-01): partial, case-insensitive match on full_name;</li>
 *   <li>NONE  - anything else (e.g. "5", "HV000123", a partial phone, "abc123"): matches nothing (AC-09).</li>
 * </ol>
 * The search box never looks members up by code or id. Shared by MemberDAO and RegistrationDAO so the rule
 * lives in one place. The keyword is trimmed first.
 */
public final class MemberKeyword {

    /** Kind of search a keyword stands for. */
    public enum Type { PHONE, NAME, NONE }

    private final Type type;
    private final String value;

    private MemberKeyword(Type type, String value) {
        this.type = type;
        this.value = value;
    }

    /**
     * Classify a raw keyword. Returns null when the keyword is null or blank (= no search at all).
     */
    public static MemberKeyword parse(String raw) {
        if (raw == null || raw.trim().isEmpty()) {
            return null;
        }
        String keyword = raw.trim();
        if (ValidationUtil.isValidPhone(keyword)) {
            return new MemberKeyword(Type.PHONE, keyword);
        }
        if (!containsDigit(keyword)) {
            return new MemberKeyword(Type.NAME, keyword);
        }
        return new MemberKeyword(Type.NONE, keyword);
    }

    public Type getType() {
        return type;
    }

    /**
     * The phone (PHONE) or the trimmed text (NAME, NONE).
     */
    public String getValue() {
        return value;
    }

    /**
     * LIKE pattern of a NAME keyword: %text% with % _ [ and the escape character itself escaped.
     * Use with "LIKE ? ESCAPE '\'".
     */
    public String getLikePattern() {
        StringBuilder sb = new StringBuilder("%");
        for (int i = 0; i < value.length(); i++) {
            char c = value.charAt(i);
            if (c == '\\' || c == '%' || c == '_' || c == '[') {
                sb.append('\\');
            }
            sb.append(c);
        }
        return sb.append('%').toString();
    }

    private static boolean containsDigit(String s) {
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c >= '0' && c <= '9') {
                return true;
            }
        }
        return false;
    }
}
