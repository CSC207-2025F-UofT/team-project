package use_case.signup;
/** Password hashing and verification abstraction implemented in the outer layer. */
public interface PasswordHasher {
    String hash(String rawPassword);
    boolean matches(String rawPassword, String encodedHash);
}
