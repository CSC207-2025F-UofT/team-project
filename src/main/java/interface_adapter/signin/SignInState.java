package interface_adapter.signin;

public class SignInState {
    private String username = "";
    private String SigninError = "";
    private String password = "";

    public String getUsernameError() {
        return "SIGN IN STATE: username error";
    }

    public String getPasswordError() {
        return "sign in state: password error";
    }

    public String getUsername() { return username;}

    public String getPassword() { return password;}

    public String getSigninError() {
        return SigninError;
    }

    public void setSigninError(String usernameError) {
        this.SigninError = usernameError;
    }

    public void setUsername(String username) { this.username = username; }

    public void setPassword(String password) { this.password = password; }

    public void clearState() {
        SigninError = "";
        username = "";
        password = "";
    }

}
