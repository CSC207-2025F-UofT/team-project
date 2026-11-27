package use_case.signup;

/**
 * The Input Data for the Sign Up use case.
 */
public class SignupInputData {

    private final String username;
    private final String password;
    private final String repeatPassword;

    /**
     * Creates the input data.
     *
     * @param username the username typed by the user
     * @param password the password typed by the user
     * @param repeatPassword the password typed again in the "Confirm Password" box
     */
    public SignupInputData(String username, String password, String repeatPassword) {
        this.username = username;
        this.password = password;
        this.repeatPassword = repeatPassword;
    }

    String getUsername() {
        return username;
    }

    String getPassword() {
        return password;
    }

    String getRepeatPassword() {
        return repeatPassword;
    }
}
