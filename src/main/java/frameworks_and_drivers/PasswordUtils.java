package frameworks_and_drivers;

import use_case.signup.PasswordHasher;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Base64;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

public class PasswordUtils implements PasswordHasher {
    private static final int ITERATIONS = 600000;
    private static final int KEY_LENGTH = 256;

    @Override
    public String hash(String password) {

        byte[] salt = new byte[16];

        new SecureRandom().nextBytes(salt);

        return "pbkdf2-sha256:" + ITERATIONS + ":"
                + Base64.getEncoder().encodeToString(salt)
                + ":"
                + Base64.getEncoder().encodeToString(
                generateHash(password, salt, ITERATIONS));
    }

    private static byte[] generateHash(String password, byte[] salt, int iterations) {

        PBEKeySpec spec = new PBEKeySpec(
                password.toCharArray(),
                salt,
                iterations,
                KEY_LENGTH);

        try {
            return SecretKeyFactory
                    .getInstance("PBKDF2WithHmacSHA256")
                    .generateSecret(spec)
                    .getEncoded();

        } catch (GeneralSecurityException ex) {
            throw new IllegalStateException(
                    "Password hashing unavailable.", ex);

        } finally {
            spec.clearPassword();
        }
    }

    public static boolean verifyPassword(String password, String stored) {

        if (password == null || stored == null) {
            return false;
        }

        try {
            String[] parts = stored.split(":", -1);

            if (parts.length != 4 || !parts[0].equals("pbkdf2-sha256")) {
                return false;
            }

            int iterations = Integer.parseInt(parts[1]);

            if (iterations < 1 || iterations > 2000000) {
                return false;
            }

            byte[] salt = Base64.getDecoder().decode(parts[2]);

            byte[] expected = Base64.getDecoder().decode(parts[3]);

            if (salt.length != 16 || expected.length != 32) {
                return false;
            }

            return MessageDigest.isEqual(expected, generateHash(password, salt, iterations));

        } catch (IllegalArgumentException ex) {
            return false;
        }
    }
}
