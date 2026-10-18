package group4.gym.management.util;

/**
 * Hashing and verification of passwords (CN-36, CL-07). Plaintext passwords are never stored (-50% penalty otherwise).
 * Suggestion: PBKDF2WithHmacSHA256 + random salt (javax.crypto, built into the JDK) or BCrypt.
 */
public final class PasswordUtil {

    private PasswordUtil() {
    }

    /**
     * Hash a raw password.
     */
    public static String hash(String rawPassword) {
        // TODO: Generate a random salt, hash rawPassword, and join algorithm/iterations/salt/hash into one string for the DB.
        // Output: a securely hashed string (it must not contain the raw password).
        return null;
    }

    /**
     * Verify a raw password against a stored hash string.
     */
    public static boolean verify(String rawPassword, String storedHash) {
        // TODO: Split the salt/parameters from storedHash, hash rawPassword again and compare in constant time.
        // Output: true if they match; false if they do not match or storedHash is invalid.
        return false;
    }
}
