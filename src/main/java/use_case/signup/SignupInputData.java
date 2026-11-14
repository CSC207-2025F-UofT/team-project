package use_case.signup;

/**
 * The Input Data for the Signup Use Case.
 */
public class SignupInputData {

    private final String username;
    private final String password;

    public SignupInputData(String username, String password) {
        this.username = username;
        this.password = password;
    }

    String getUsername(){
        return this.username;
    }

    String getPassword(){
        return this.password;
    }
}
