package interface_adapter.loggedInHomePage;

import interface_adapter.ViewModel;

public class LoggedInHomePageViewModel extends ViewModel<LoggedInHomePageState> {
    public static final String TO_SEARCH_LABEL = "Go to Search Page";
    public static final String TO_FAVOURITES_LABEL = "Go to Favourites Page";

    public LoggedInHomePageViewModel() {
        super("Logged In Home page");
        setState(new LoggedInHomePageState());
    }
}
