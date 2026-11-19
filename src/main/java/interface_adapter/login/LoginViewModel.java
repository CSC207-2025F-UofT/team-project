package interface_adapter.login;

import interface_adapter.ViewModel;

/**
 * View model for the login screen.
 */
public class LoginViewModel extends ViewModel<LoginState> {
    public static final String VIEW_NAME = "log in";

    public LoginViewModel() {
        super(VIEW_NAME);
        setState(new LoginState());
    }
}
