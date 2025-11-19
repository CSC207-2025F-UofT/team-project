package interface_adapter.login;

import interface_adapter.ViewModel;

public class LoginViewModel extends ViewModel<LoginState> {
    public static final String LOGIN_ERROR_MESSAGE = "Username and password do not match. Please try again!";
    public LoginViewModel() {
        super("login");
        setState(new LoginState());
    }
}
