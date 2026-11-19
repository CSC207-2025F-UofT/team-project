package interface_adapter.signup;

/**
 * What the signup screen currently shows.
 */
public class SignupState {
    private String username = "";
    private String error = null;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
