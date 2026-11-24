package data_access;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

import use_case.PasswordHasher;

/**
 * Salted SHA-256 password hashing. Stored format: "salt$hash" (both Base64).
 * A random salt means two users with the same password still get different hashes.
 */
public class Sha256PasswordHasher implements PasswordHasher {
    private static final int SALT_BYTES = 16;
    private static final String SEPARATOR = "$";

    private final SecureRandom random = new SecureRandom();

    @Override
    public String hash(String rawPassword) {
        final byte[] salt = new byte[SALT_BYTES];
        random.nextBytes(salt);
        return encode(salt) + SEPARATOR + encode(digest(salt, rawPassword));
    }

    @Override
    public boolean matches(String rawPassword, String storedHash) {
        if (rawPassword == null || storedHash == null) {
            return false;
        }
        final int sep = storedHash.indexOf(SEPARATOR);
        if (sep < 0) {
            return false;
        }
        try {
            final byte[] salt = Base64.getDecoder().decode(storedHash.substring(0, sep));
            final byte[] expected = Base64.getDecoder().decode(storedHash.substring(sep + 1));
            return MessageDigest.isEqual(expected, digest(salt, rawPassword));
        }
        catch (IllegalArgumentException e) {
            // The stored value is not valid Base64, so it can't match.
            return false;
        }
    }

    private static byte[] digest(byte[] salt, String rawPassword) {
        try {
            final MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(salt);
            return md.digest(rawPassword.getBytes(StandardCharsets.UTF_8));
        }
        catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("SHA-256 is not available", e);
        }
    }

    private static String encode(byte[] bytes) {
        return Base64.getEncoder().encodeToString(bytes);
    }
}
