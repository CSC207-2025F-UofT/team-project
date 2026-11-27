package interface_adapter.login;

import java.util.ArrayList;
import java.util.List;

/**
 * The View Model for the login screen. Holds the LoginState and tells the
 * LoginView when it changes.
 */
public class LoginViewModel {

    /** The name the login screen is registered under in the ViewManager. */
    public static final String VIEW_NAME = "log in";

    private final List<LoginViewModelListener> listeners = new ArrayList<>();
    private LoginState state = new LoginState();

    public LoginState getState() {
        return state;
    }

    public void setState(LoginState state) {
        this.state = state;
    }

    /**
     * Registers a listener to be told when the state changes.
     *
     * @param listener the listener to add
     */
    public void addListener(LoginViewModelListener listener) {
        listeners.add(listener);
    }

    /**
     * Tells every listener that the state has changed.
     */
    public void notifyStateChanged() {
        for (LoginViewModelListener listener : listeners) {
            listener.stateChanged();
        }
    }
}