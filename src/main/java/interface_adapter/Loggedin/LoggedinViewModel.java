package interface_adapter.Loggedin;

import java.util.ArrayList;
import java.util.List;

/**
 * The View Model for the Logged-in View.
 */
public class LoggedinViewModel {
    public static final String VIEW_NAME = "logged in";

    private final List<LoggedinViewModelListener> listeners = new ArrayList<>();
    private LoggedinState state = new LoggedinState();

    public LoggedinState getState() {
        return this.state;
    }

    /** Stores the state only. Call notifyStateChanged() to tell the view. */
    public void setState(LoggedinState state) {
        this.state = state;
    }

    public void addListener(LoggedinViewModelListener listener) {
        listeners.add(listener);
    }

    public void notifyStateChanged() {
        for (LoggedinViewModelListener listener : listeners) {
            listener.stateChanged();
        }
    }
}
