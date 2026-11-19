package interface_adapter.logged_in;

/**
 * State for the logged-in screen (the dashboard).
 * SHARED: the dashboard owner can add fields here (e.g. totals to display).
 */
public class LoggedInState {
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
