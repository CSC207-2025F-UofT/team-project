package interface_adapter.Signup;

/**
 * The state for the Signup View.
 */
public class SignupState {
    private String username = "";
    private boolean researcher = false;
    private String signupError = "";

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public boolean isResearcher() {
        return researcher;
    }

    public void setResearcher(boolean researcher) {
        this.researcher = researcher;
    }

    public String getSignupError() {
        return signupError;
    }

    public void setSignupError(String signupError) {
        this.signupError = signupError;
    }
}
