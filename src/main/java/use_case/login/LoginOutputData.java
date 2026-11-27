package use_case.login;

/**
 * The Output Data for the Login use case.
 */
public class LoginOutputData {

    private final String username;

    /**
     * Creates the output data.
     *
     * @param username the username of the player who logged in
     */
    public LoginOutputData(String username) {
        this.username = username;
    }

    /**
     * Returns the username of the player who logged in.
     *
     * @return the username
     */
    public String getUsername() {
        return username;
    }
}
