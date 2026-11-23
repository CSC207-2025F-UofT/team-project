package use_case.user;

public interface PasswordHasher {
    String hash(String password);
    boolean verify(String password, String storedHash);
}
