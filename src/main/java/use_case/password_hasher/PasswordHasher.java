package use_case.password_hasher;

public interface PasswordHasher {
    String generateSalt();
    String hash(String password, String salt);
    boolean verify(String password, String salt, String hash);
}
