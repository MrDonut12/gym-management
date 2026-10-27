package group4.gym.management.util;

import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Base64;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

/**
 * Password hashing and verification (CL-03), using only the JDK.
 * Stored format: pbkdf2$iterations$Base64(salt)$Base64(hash).
 * New hashes are 83 ASCII characters and fit accounts.password_hash VARCHAR(100).
 * Passwords are used exactly as entered: no trimming or normalization (HL-09).
 */
public final class PasswordUtil {

    private static final String ALGORITHM = "PBKDF2WithHmacSHA256";
    private static final String PREFIX = "pbkdf2";
    private static final int ITERATIONS = 600_000;
    // Bound work when parsing a corrupt hash; keep old hashes valid if cost rises.
    private static final int MIN_ITERATIONS = 600_000;
    private static final int MAX_ITERATIONS = 1_000_000;
    private static final int SALT_BYTES = 16;
    private static final int HASH_BYTES = 32;
    private static final SecureRandom RANDOM = new SecureRandom();
    private static final Base64.Encoder ENCODER = Base64.getEncoder();
    private static final Base64.Decoder DECODER = Base64.getDecoder();

    private PasswordUtil() {
    }

    /**
     * Hash an 8-64 character password with a new random salt.
     *
     * @throws IllegalArgumentException if the password is null or outside 8-64 characters
     * @throws IllegalStateException if the JDK cannot provide the hashing algorithm
     */
    public static String hash(String rawPassword) {
        if (!isValidLength(rawPassword)) {
            throw new IllegalArgumentException("Password must contain 8-64 characters.");
        }

        byte[] salt = new byte[SALT_BYTES];
        RANDOM.nextBytes(salt);
        byte[] digest = deriveKey(rawPassword, salt, ITERATIONS);
        try {
            return PREFIX + "$" + ITERATIONS + "$"
                    + ENCODER.encodeToString(salt) + "$" + ENCODER.encodeToString(digest);
        } finally {
            Arrays.fill(digest, (byte) 0);
        }
    }

    /**
     * Verify against a stored hash. Invalid input or malformed hashes return false.
     * Uses MessageDigest.isEqual for the fixed-size digest comparison.
     */
    public static boolean verify(String rawPassword, String storedHash) {
        if (!isValidLength(rawPassword) || storedHash == null || storedHash.length() >= 100) {
            return false;
        }

        byte[] salt;
        byte[] expected;
        int iterations;
        try {
            String[] parts = storedHash.split("\\$", -1);
            if (parts.length != 4 || !PREFIX.equals(parts[0])
                    || !parts[1].matches("[1-9][0-9]{0,6}")) {
                return false;
            }
            iterations = Integer.parseInt(parts[1]);
            if (iterations < MIN_ITERATIONS || iterations > MAX_ITERATIONS) {
                return false;
            }

            salt = DECODER.decode(parts[2]);
            expected = DECODER.decode(parts[3]);
            if (salt.length != SALT_BYTES || expected.length != HASH_BYTES
                    || !ENCODER.encodeToString(salt).equals(parts[2])
                    || !ENCODER.encodeToString(expected).equals(parts[3])) {
                return false;
            }
        } catch (IllegalArgumentException ex) {
            // Bad iteration count or Base64 is invalid stored data, not a login error.
            return false;
        }

        byte[] actual = deriveKey(rawPassword, salt, iterations);
        try {
            return MessageDigest.isEqual(expected, actual);
        } finally {
            Arrays.fill(actual, (byte) 0);
            Arrays.fill(expected, (byte) 0);
        }
    }

    private static boolean isValidLength(String rawPassword) {
        return rawPassword != null && rawPassword.length() >= 8 && rawPassword.length() <= 64;
    }

    private static byte[] deriveKey(String rawPassword, byte[] salt, int iterations) {
        char[] password = rawPassword.toCharArray();
        PBEKeySpec spec = new PBEKeySpec(password, salt, iterations, HASH_BYTES * 8);
        Arrays.fill(password, '\0');
        try {
            return SecretKeyFactory.getInstance(ALGORITHM).generateSecret(spec).getEncoded();
        } catch (GeneralSecurityException ex) {
            throw new IllegalStateException("PBKDF2WithHmacSHA256 is unavailable.", ex);
        } finally {
            spec.clearPassword();
        }
    }
}
