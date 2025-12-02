package use_case.user_logout;

/**
 * Output Data for the Logout Use Case.
 */
public class UserLogoutOutputData {

    private final String username;

    public UserLogoutOutputData(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }
}
