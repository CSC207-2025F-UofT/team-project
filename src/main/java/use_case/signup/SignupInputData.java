package use_case.signup;

/**
 * Data structure for Signup input.
 * Contains the username and password provided by the user
 * during the signup process.
 */
public class SignupInputData {
    private final String username;
    private final String password;
    private final String confirmPassword;

    /**
     * Constructs a SignupInputData with the given credentials.
     * @param username the desired username
     * @param password the desired password
     * @param confirmPassword the retyped password (may or may not match password)
     */
    public SignupInputData(String username, String password, String confirmPassword) {
        this.username = username;
        this.password = password;
        this.confirmPassword = confirmPassword;
    }

    String getUsername() { return username; }
    String getPassword() { return password; }
    String getConfirmPassword() { return confirmPassword; }
}