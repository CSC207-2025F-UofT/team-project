package interface_adapter.settings;

import interface_adapter.ViewModel;
import interface_adapter.loggedInHomePage.LoggedInHomePageState;

public class SettingsState {
    private String username = "";

    private String newPassword = "";
    private String repeatPassword = "";
    private String passwordError = "";
    private int clear = 0;

    public SettingsState(SettingsState copy) {
        username = copy.username;
        newPassword = copy.newPassword;
        repeatPassword = copy.repeatPassword;
        passwordError = copy.passwordError;
    }

    // Because of the previous copy constructor, the default constructor must be explicit.
    public SettingsState() {

    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setNewPassword(String password) {
        this.newPassword = password;
    }

    public void setRepeatPassword(String repeatPassword) {
        this.repeatPassword = repeatPassword;
    }

    public void setPasswordError(String passwordError) {
        this.passwordError = passwordError;
    }

    public String getPasswordError() {return passwordError;}

    public String getNewPassword() {
        return newPassword;
    }

    public String getRepeatPassword() {
        return repeatPassword;
    }

    public void clearState() {
        username = "";
        newPassword = "";
        repeatPassword = "";
        passwordError = "";
        clear ++;
    }
}
