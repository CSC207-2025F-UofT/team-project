package use_case.logout;

/**
 * Result of a successful logout: who was logged out.
 */
public class LogoutOutputData {
    private final String username;

    public LogoutOutputData(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }
}
