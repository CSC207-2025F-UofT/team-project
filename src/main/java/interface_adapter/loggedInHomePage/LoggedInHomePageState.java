package interface_adapter.loggedInHomePage;

public class LoggedInHomePageState {
    private String username = "";

    private String password = "";
    private String passwordError = "";


    public LoggedInHomePageState(LoggedInHomePageState copy) {
        username = copy.username;
        password = copy.password;
        passwordError = copy.passwordError;
    }

    // Because of the previous copy constructor, the default constructor must be explicit.
    public LoggedInHomePageState() {

    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setPasswordError(String passwordError) {
        this.passwordError = passwordError;
    }

    public String getPasswordError() {return passwordError;}

    public String getPassword() {
        return password;
    }

    public void clearState() {
        username = "";
        password = "";
        passwordError = "";
    }

}