package interface_adapter.login;

/**
 * The state for the Login View Model.
 */
public class LoginState {
    private String username = "";
    private String loginError;
    private String password = "";

    // Copy constructor for creating defensive copies
    public LoginState(LoginState copy) {
        username = copy.username;
        loginError = copy.loginError;
        password = copy.password;
    }

    // Default constructor
    public LoginState() {
    }

    public String getUsername() {
        return username;
    }

    public String getLoginError() {
        return loginError;
    }

    public String getPassword() {
        return password;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setLoginError(String loginError) {
        this.loginError = loginError;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
