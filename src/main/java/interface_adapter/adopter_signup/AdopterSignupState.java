package interface_adapter.adopter_signup;

/**
 * What the signup screen should show. Passwords are deliberately not kept here.
 */
public class AdopterSignupState {
    private String username = "";
    private String error;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    /** Returns the error to show, or null if there is none. */
    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
