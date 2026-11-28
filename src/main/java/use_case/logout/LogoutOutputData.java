package use_case.logout;

/**
 * Output Data for the Log Out Use Case.
 */
public class LogoutOutputData {

    private final boolean wasLoggedIn;

    /**
     * Creates the output data.
     * @param wasLoggedIn whether a user was logged in before this request
     */
    public LogoutOutputData(boolean wasLoggedIn) {
        this.wasLoggedIn = wasLoggedIn;
    }

    /**
     * Returns whether a user was logged in before this request.
     * @return true if a user was logged in
     */
    public boolean wasLoggedIn() {
        return wasLoggedIn;
    }
}
