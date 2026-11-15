package interface_adapter.signup;

/**
 * The state for the Signup View Model.
 */
public class LoginSignupState {

    private String username = "";
    private String password = "";
    private String signupError = "";

    public String getUsername(){
        return username;
    }

    public String getPassword(){
        return password;
    }

    public String getSignupError(){
        return signupError;
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
}
