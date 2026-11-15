package interface_adapter.login_signup;

/**
 * The state for the LoginSignup View Model.
 */
public class LoginSignupState {

    private String username = "";
    private String password = "";
    private String signupError = "";
    private String loginError = "";

    public String getUsername(){
        return username;
    }

    public String getPassword(){
        return password;
    }

    public String getSignupError(){
        return signupError;
    }

    public String getLoginError(){
        return loginError;
    }

    public void setUsername(String username){
        this.username = username;
    }

    public void setPassword(String password){
        this.password = password;
    }

    public void setSignupError(String signupError){
        this.signupError = signupError;
    }

    public void setLoginError(String loginError){
        this.loginError = loginError;
    }
}
