package interface_adapter.login;

import interface_adapter.ViewModel;

public final class LogInViewModel extends ViewModel<LogInState> {
    public static final String VIEW_NAME = "Log In";

    public LogInViewModel() {
        super(VIEW_NAME, LogInState.empty());
    }
}
