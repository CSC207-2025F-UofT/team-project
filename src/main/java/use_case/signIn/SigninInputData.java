package use_case.signIn;

/**
 * The Input Data for the Login Use Case.
 */
public class SigninInputData {

    private final String username;
    private final String password;

    public SigninInputData(String username, String password) {
        this.username = username;
        this.password = password;
    }

    String getUsername() {
        return this.username;
    }

    String getPassword() {
        return this.password;
    }

}