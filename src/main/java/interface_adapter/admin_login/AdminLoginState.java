package interface_adapter.admin_login;

/**
 * What the admin login screen should show.
 */
public class AdminLoginState {
    private String error;

    /** Returns the error to show, or null if there is none. */
    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
