package group4.gym.management.util;

import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Base64;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

public final class PasswordUtil {
    private static final int ITERATIONS = 600000;
    private PasswordUtil() { }

    /** PBKDF2-HMAC-SHA256: iterations:base64-salt:base64-hash. */
    public static String hash(String rawPassword) {
        if (rawPassword == null) throw new IllegalArgumentException("Password is required");
        byte[] salt = new byte[16];
        new SecureRandom().nextBytes(salt);
        return ITERATIONS + ":" + Base64.getEncoder().encodeToString(salt) + ":"
                + Base64.getEncoder().encodeToString(derive(rawPassword, salt, ITERATIONS));
    }

    public static boolean verify(String rawPassword, String storedHash) {
        if (rawPassword == null || storedHash == null) return false;
        try {
            int iterations;
            byte[] salt;
            byte[] expected;
            // T01 seed uses the existing hex encoding; new hashes use Base64.
            if (storedHash.startsWith("pbkdf2_sha256$")) {
                String[] parts = storedHash.split("\\$", -1);
                if (parts.length != 4) return false;
                iterations = Integer.parseInt(parts[1]);
                salt = decodeHex(parts[2]);
                expected = decodeHex(parts[3]);
            } else {
                String[] parts = storedHash.split(":", -1);
                if (parts.length != 3) return false;
                iterations = Integer.parseInt(parts[0]);
                salt = Base64.getDecoder().decode(parts[1]);
                expected = Base64.getDecoder().decode(parts[2]);
            }
            if (iterations < 1 || iterations > 2000000 || salt.length < 16 || expected.length != 32) return false;
            return MessageDigest.isEqual(expected, derive(rawPassword, salt, iterations));
        } catch (IllegalArgumentException ex) {
            return false;
        }
    }

    private static byte[] derive(String password, byte[] salt, int iterations) {
        PBEKeySpec spec = new PBEKeySpec(password.toCharArray(), salt, iterations, 256);
        try {
            return SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256").generateSecret(spec).getEncoded();
        } catch (GeneralSecurityException ex) {
            throw new IllegalStateException("Password hashing is unavailable", ex);
        } finally {
            spec.clearPassword();
        }
    }

    private static byte[] decodeHex(String value) {
        if (value.length() % 2 != 0) throw new IllegalArgumentException("Invalid hex");
        byte[] result = new byte[value.length() / 2];
        for (int i = 0; i < result.length; i++) {
            int high = Character.digit(value.charAt(i * 2), 16);
            int low = Character.digit(value.charAt(i * 2 + 1), 16);
            if (high < 0 || low < 0) throw new IllegalArgumentException("Invalid hex");
            result[i] = (byte) ((high << 4) | low);
        }
        return result;
    }
}
