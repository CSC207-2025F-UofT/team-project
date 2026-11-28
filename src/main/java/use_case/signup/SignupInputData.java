package use_case.signup;

/**
 * The Input Data for the Signup Use Case.
 * Preferences are filled in later (Use Case 4), not at signup.
 */
public class SignupInputData {

    private final String username;
    private final String id;
    private final String email;
    private final String password;
    private final String repeatPassword;

    public SignupInputData(String username, String id, String email, String password,
                           String repeatPassword) {
        this.username = username;
        this.id = id;
        this.email = email;
        this.password = password;
        this.repeatPassword = repeatPassword;
    }

    String getUsername() {
        return username;
    }

    String getId() {return id;}

    String getEmail() {return email;}

    String getPassword() {
        return password;
    }

    public String getRepeatPassword() {
        return repeatPassword;
    }
}
