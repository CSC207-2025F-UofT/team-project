package interface_adapter.loggedInSearch;

import interface_adapter.ViewModel;

public class LoggedInSearchViewModel extends ViewModel<LoggedInSearchState> {

    public LoggedInSearchViewModel() {
        super("Logged In Search View");
        setState(new LoggedInSearchState());
    }
}
