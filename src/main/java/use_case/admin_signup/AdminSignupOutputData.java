package use_case.admin_signup;

/**
 * Output Data for the Admin Signup Use Case.
 */
public class AdminSignupOutputData {

    private final String email;

    public AdminSignupOutputData(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }
}
