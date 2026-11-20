package interface_adapter.signup;

public class SignupState {

    private String username = "";
    private String password = "";
    private String repeatPassword = "";
    private String signupError;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRepeatPassword() {
        return repeatPassword;
    }

    public void setRepeatPassword(String repeatPassword) {
        this.repeatPassword = repeatPassword;
    }

    public String getSignupError() {
        return signupError;
    }

    public void setSignupError(String signupError) {
        this.signupError = signupError;
    }
}
