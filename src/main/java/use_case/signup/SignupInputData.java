package use_case.signup;

/**
 * The Input Data for the Signup Use Case.
 */
public class SignupInputData {

    private final String username;
    private final String id;
    private final String email;
    private final String password;
    private final String repeatPassword;
    private final String preference;


    public SignupInputData(String username, String id, String email, String password,
                           String repeatPassword, String preference) {
        this.username = username;
        this.id = id;
        this.email = email;
        this.password = password;
        this.repeatPassword = repeatPassword;
        this.preference = preference;
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

    String getPreference() {return preference;}
}
