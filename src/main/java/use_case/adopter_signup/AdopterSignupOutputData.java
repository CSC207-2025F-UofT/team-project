package use_case.adopter_signup;

/**
 * Output data for the adopter signup use case.
 */
public class AdopterSignupOutputData {
    private final String username;

    public AdopterSignupOutputData(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }
}
