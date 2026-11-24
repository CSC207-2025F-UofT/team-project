package use_case.adopter_signup;

/**
 * Input data for the adopter signup use case.
 */
public class AdopterSignupInputData {
    private final String username;
    private final String password;
    private final String repeatPassword;

    public AdopterSignupInputData(String username, String password, String repeatPassword) {
        this.username = username;
        this.password = password;
        this.repeatPassword = repeatPassword;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getRepeatPassword() {
        return repeatPassword;
    }
}
