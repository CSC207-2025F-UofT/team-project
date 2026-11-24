package interface_adapter.Signup;

import java.util.ArrayList;
import java.util.List;

/**
 * The View Model for the Signup View.
 */
public class SignupViewModel {
    public static final String VIEW_NAME = "sign up";

    private final List<SignupViewModelListener> listeners = new ArrayList<>();
    private SignupState state = new SignupState();

    public SignupState getState() {
        return this.state;
    }

    /** Stores the state only. Call notifyStateChanged() to tell the view. */
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
