package use_case.welcome_menu;

/**
 * Output Data for the Welcome Menu.
 */
public class WelcomeOutputData {
    private final String username;

    public WelcomeOutputData(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }
}
