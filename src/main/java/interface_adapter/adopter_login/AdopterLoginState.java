package interface_adapter.adopter_login;

/**
 * What the adopter login screen should show.
 */
public class AdopterLoginState {
    private String username = "";
    private String error;
    private String notice;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    /** Returns the error to show in red, or null if there is none. */
    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    /** Returns a non-error message (e.g. "Account created!"), or null. */
    public String getNotice() {
        return notice;
    }

    public void setNotice(String notice) {
        this.notice = notice;
    }
}
