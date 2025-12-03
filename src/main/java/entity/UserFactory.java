package entity;

import java.util.Base64;
import java.util.UUID;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Factory for creating CommonUser objects.
 */
public class UserFactory {

    public User createUser(String name, String passwordHash) {
        return new User(UUID.randomUUID().toString(), name, passwordHash);
    }

    public User loadUser(String userId, String name, String passwordHash) {
        return new User(userId, name, passwordHash);
    }

    public static String hashPasswordSHA256(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            // Get the hash in bytes
            byte[] hashBytes = digest.digest(password.getBytes(StandardCharsets.UTF_8));
            // Return the hashed password
            return Base64.getEncoder().encodeToString(hashBytes);

        } catch (NoSuchAlgorithmException e) {
            // This exception should not happen for standard algorithms like SHA-256
            throw new RuntimeException("SHA-256 algorithm not available", e);
        }
    }
}
