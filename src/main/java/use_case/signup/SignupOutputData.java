package use_case.signup;

/**
 * The Output Data for the Sign Up use case.
 */
public class SignupOutputData {

    private final String username;

    /**
     * Creates the output data.
     *
     * @param username the username of the new account
     */
    public SignupOutputData(String username) {
        this.username = username;
    }

    /**
     * Returns the username of the new account.
     *
     * @return the username
     */
    public String getUsername() {
        return username;
    }
}
