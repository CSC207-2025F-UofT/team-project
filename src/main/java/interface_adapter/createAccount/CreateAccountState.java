package interface_adapter.createAccount;

public class CreateAccountState {

    private String username = "";
    private String password = "";
    private String repeatPassword = "";

    private String usernameError;
    private String passwordError;
    private String repeatPasswordError;

    private int clear = 0;

    public CreateAccountState() {
    }

    public CreateAccountState(CreateAccountState copy) {
        this.username = copy.username;
        this.password = copy.password;
        this.repeatPassword = copy.repeatPassword;
        this.usernameError = copy.usernameError;
        this.passwordError = copy.passwordError;
        this.repeatPasswordError = copy.repeatPasswordError;
        this.clear = copy.clear;
    }

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

    public String getUsernameError() {
        return usernameError;
    }

    public void setUsernameError(String usernameError) {
        this.usernameError = usernameError;
    }

    public String getPasswordError() {
        return passwordError;
    }

    public void setPasswordError(String passwordError) {
        this.passwordError = passwordError;
    }

    public String getRepeatPasswordError() {
        return repeatPasswordError;
    }

    public void setRepeatPasswordError(String repeatPasswordError) {
        this.repeatPasswordError = repeatPasswordError;
    }

    public void clearState() {
        // reset user inputs
        this.username = "";
        this.password = "";
        this.repeatPassword = "";

        // reset errors
        this.usernameError = null;
        this.passwordError = null;
        this.repeatPasswordError = null;

        clear ++;
    }
}
