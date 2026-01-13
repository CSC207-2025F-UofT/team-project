package interface_adapter.loggedInHomePage;

import interface_adapter.ViewModel;

public class LoggedInHomePageViewModel extends ViewModel<LoggedInHomePageState> {
    public static final String TO_SEARCH_LABEL = "Search Weather";
    public static final String TO_FAVOURITES_LABEL = "My Favourites";
    public static final String WELCOME_LABEL = "Welcome";
    public static final String LOGOUT_LABEL = "Logout";
    public static final String CHANGEPASSWORD_LABEL = "Change Password";
    public static final String REPEAT_PASSWORD_LABEL = "Repeat Password";
    public static final String SETTINGS_LABEL = "Settings";

    public LoggedInHomePageViewModel() {
        super("logged in home page");
        setState(new LoggedInHomePageState());
    }
}
