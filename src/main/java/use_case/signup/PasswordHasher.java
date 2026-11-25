package use_case.signup;

public interface PasswordHasher {
    String hash(String password);
}
