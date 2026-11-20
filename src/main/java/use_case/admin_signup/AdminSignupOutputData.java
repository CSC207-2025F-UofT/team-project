package use_case.admin_signup;

/**
 * Output Data for the Admin Signup Use Case.
 */
public class AdminSignupOutputData {

    private final String username;

    public AdminSignupOutputData(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }
}
