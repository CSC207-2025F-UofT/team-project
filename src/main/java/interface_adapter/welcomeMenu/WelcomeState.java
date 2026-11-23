package interface_adapter.welcomeMenu;

public class WelcomeState {

    private String username = "";
    private String loginError;

    public String getUsername() {
        return username;
    }

    public String getLoginError() {
        return loginError;
    }
    public void setUsername(String username) {
        this.username = username;
    }

    public void setLoginError(String usernameError) {
        this.loginError = usernameError;
    }
}
