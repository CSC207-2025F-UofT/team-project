package interface_adapter.signup;

import java.util.ArrayList;
import java.util.List;

public class LoginSignupViewModel {

    public static final String VIEW_NAME = "login signup";
    public static final String USERNAME_LABEL = "Username";
    public static final String PASSWORD_LABEL = "Password";
    public static final String LOGIN_BUTTON_LABEL = "Login";
    public static final String SIGNUP_BUTTON_LABEL = "Sign Up";

    private final List<LoginSignupViewModelListener> listeners = new ArrayList<>();

    private LoginSignupState state = new LoginSignupState();

    public LoginSignupState getState() {
        return state;
    }

    public void setState(LoginSignupState state) {
        this.state = state;
    }

    public void addListener(LoginSignupViewModelListener listener) {
        listeners.add(listener);
    }

    public void notifyStateChanged() {
        for (LoginSignupViewModelListener listener : listeners) {
            listener.stateChanged();
        }
    }
}
