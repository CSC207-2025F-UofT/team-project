package interface_adapter.logged_in;

import interface_adapter.ViewModel;

/**
 * View model for the screen shown after a successful login.
 * Teammates can replace this with the real adopter home page / admin dashboard.
 */
public class LoggedInViewModel extends ViewModel<LoggedInState> {
    public static final String VIEW_NAME = "logged in";

    public LoggedInViewModel() {
        super(VIEW_NAME);
        setState(new LoggedInState());
    }
}
