package use_case.signup;

/**
 * The Input Data for the Signup Use Case.
 */
public class SignupInputData {

    private final String email;
    private final String username;
    private final String password;
    private final String repeatPassword;

    public SignupInputData(String email, String username, String password, String repeatPassword) {
        this.email = email;
        this.username = username;
        this.password = password;
        this.repeatPassword = repeatPassword;
    }

    String getEmail() {
        return this.email;
    }

    String getUsername() {
        return username;
    }

    String getPassword() {
        return password;
    }

    public String getRepeatPassword() {
        return repeatPassword;
    }
}
