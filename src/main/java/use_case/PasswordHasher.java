package use_case;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Hashes passwords for the signup, admin signup and login use cases,
 * so all three always use the same algorithm.
 */
public final class PasswordHasher {
    static final String ALGORITHM = "SHA-256";

    private PasswordHasher() {
    }

    /**
     * Hashes the raw password with SHA-256 and returns it as a lowercase hex string.
     * @param rawPassword the password the user typed
     * @return the 64-character hex hash
     */
    public static String hash(String rawPassword) {
        return hash(rawPassword, ALGORITHM);
    }

    /**
     * Hashes the raw password with the given algorithm.
     * @param rawPassword the password the user typed
     * @param algorithm the message digest algorithm
     * @return the hash as a lowercase hex string, two characters per byte
     * @throws IllegalStateException if the algorithm is not available
     */
    static String hash(String rawPassword, String algorithm) {
        try {
            final MessageDigest digest = MessageDigest.getInstance(algorithm);
            final byte[] hash = digest.digest(rawPassword.getBytes(StandardCharsets.UTF_8));
            return String.format("%0" + (hash.length * 2) + "x", new BigInteger(1, hash));
        }
        catch (NoSuchAlgorithmException ex) {
            throw new IllegalStateException(algorithm + " not available", ex);
        }
    }
}
