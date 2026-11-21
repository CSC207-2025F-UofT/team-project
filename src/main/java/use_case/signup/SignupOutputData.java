package use_case.signup;

/**
 * Output Data for the Signup Use Case.
 */
public class SignupOutputData {

    private final String email;

    public SignupOutputData(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

}
