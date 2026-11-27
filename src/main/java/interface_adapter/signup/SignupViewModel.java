package interface_adapter.signup;

import java.util.ArrayList;
import java.util.List;

/**
 * The View Model for the sign up screen. Holds the SignupState and tells the
 * SignupView when it changes.
 */
public class SignupViewModel {

    /** The name the sign up screen is registered under in the ViewManager. */
    public static final String VIEW_NAME = "sign up";

    private final List<SignupViewModelListener> listeners = new ArrayList<>();
    private SignupState state = new SignupState();

    public SignupState getState() {
        return state;
    }

    public void setState(SignupState state) {
        this.state = state;
    }

    public void addListener(SignupViewModelListener listener) {
        listeners.add(listener);
    }

    public void notifyStateChanged() {
        for (SignupViewModelListener listener : listeners) {
            listener.stateChanged();
        }
    }
}