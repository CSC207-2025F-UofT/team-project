package use_case.signup;

/**
 * Output data for the Signup Use Case.
 * Contains information about the successfully created account.
 */
public class SignupOutputData {
    private final String username;

    /**
     * Constructs SignupOutputData with the new username.
     * @param username the username of the newly created account
     */
    public SignupOutputData(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }
}