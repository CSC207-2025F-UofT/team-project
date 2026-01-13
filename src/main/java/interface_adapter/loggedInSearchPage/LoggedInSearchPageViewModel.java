package interface_adapter.loggedInSearchPage;

import interface_adapter.ViewModel;

public class LoggedInSearchPageViewModel extends ViewModel<LoggedInSearchPageState> {
    public static final String TITLE_LABEL = "Weather Search";
    public static final String SEARCH_BUTTON_LABEL = "Search";
    public static final String SEARCH_BAR_LABEL = "Search";
    public static final String GO_BACK_LABEL = "Go Back";

    public LoggedInSearchPageViewModel() {
        super("Logged In Search View");
        setState(new LoggedInSearchPageState());
    }
}
