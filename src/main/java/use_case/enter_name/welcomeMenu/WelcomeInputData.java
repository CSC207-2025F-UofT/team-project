package use_case.enter_name.welcomeMenu;

/**
 * Input data for the welcome menu
 */
public class WelcomeInputData {
    private final String username;

    public WelcomeInputData(String username) {
        this.username = username;
    }

    String getUsername() {
        return username;
    }
}
