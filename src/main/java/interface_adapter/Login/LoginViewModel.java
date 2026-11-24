package interface_adapter.Login;

import java.util.ArrayList;
import java.util.List;

/**
 * The View Model for the Login View.
 */
public class LoginViewModel {
    public static final String VIEW_NAME = "log in";

    private final List<LoginViewModelListener> listeners = new ArrayList<>();
    private LoginState state = new LoginState();

    public LoginState getState() {
        return this.state;
    }

    /** Stores the state only. Call notifyStateChanged() to tell the view. */
    public void setState(LoginState state) {
        this.state = state;
    }

    public void addListener(LoginViewModelListener listener) {
        listeners.add(listener);
    }

    public void notifyStateChanged() {
        for (LoginViewModelListener listener : listeners) {
            listener.stateChanged();
        }
    }
}
