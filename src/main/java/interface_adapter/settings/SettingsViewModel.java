package interface_adapter.settings;

import interface_adapter.ViewModel;
import interface_adapter.loggedInHomePage.LoggedInHomePageState;
import interface_adapter.loggedInHomePage.LoggedInHomePageViewModel;
import interface_adapter.settings.SettingsState;

public class SettingsViewModel extends ViewModel<SettingsState> {
    public static final String DELETE_ACCOUNT_LABEL = "Delete Account";
    public static final String LOGOUT_LABEL = "Logout";
    public static final String CHANGEPASSWORD_LABEL = "Change Password";
    public static final String REPEAT_PASSWORD_LABEL = "Repeat Password";

    public SettingsViewModel() {
        super("settings");
        setState(new SettingsState());
    }
}
