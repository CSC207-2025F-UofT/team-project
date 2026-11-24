package use_case;

/**
 * Hashes and checks passwords.
 * Declared in the use case layer so interactors don't depend on a specific
 * hashing algorithm (Dependency Inversion Principle).
 */
public interface PasswordHasher {

    /** Returns a hash of the raw password that is safe to store. */
    String hash(String rawPassword);

    /** Returns true if the raw password matches the stored hash. */
    boolean matches(String rawPassword, String storedHash);
}
