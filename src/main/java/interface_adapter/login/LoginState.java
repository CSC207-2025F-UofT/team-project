package interface_adapter.login;

/**
 * The state for the Login View Model.
 */
public class LoginState {
    private String email = "";
    private String loginError;
    private String password = "";

    public String getEmail() {
        return email;
    }

    public String getLoginError() {
        return loginError;
    }

    public String getPassword() {
        return password;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setLoginError(String emailError) {
        this.loginError = emailError;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
