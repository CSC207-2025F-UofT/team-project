package use_case.adopter_login;

/**
 * Output data for the adopter login use case.
 */
public class AdopterLoginOutputData {
    private final String username;

    public AdopterLoginOutputData(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }
}
