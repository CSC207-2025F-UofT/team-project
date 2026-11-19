package interface_adapter.login;

/**
 * What the login screen currently shows.
 */
public class LoginState {
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
