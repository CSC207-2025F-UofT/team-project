package interface_adapter.user_login;

public class UserLoginState {
    private String username = "";
    private String loginError;
    private String userpassword = "";
    private String userType = "user"; 

    public String getUsername() {
        return username;
    }

    public String getLoginError() {
        return loginError;
    }

    public String getUserPassword() {
        return userpassword;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setLoginError(String usernameError) {
        this.loginError = usernameError;
    }

    public void setUserPassword(String userpassword) {
        this.userpassword = userpassword;
    }
    
    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

}
