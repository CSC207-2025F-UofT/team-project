package use_case.signIn;

/**
 * Output Data for the Login Use Case.
 */
public class SignInOutputData {

    private final String username;

    public SignInOutputData(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }
}