package interface_adapter.submit_application;

import java.util.ArrayList;
import java.util.List;

/**
 * The View Model for the Submit Application View.
 */
public final class SubmitApplicationViewModel {

    public static final String VIEW_NAME = "submit application";

    private final List<SubmitApplicationViewModelListener> listeners = new ArrayList<>();

    private SubmitApplicationState state = new SubmitApplicationState();

    public SubmitApplicationState getState() {
        return this.state;
    }

    public void setState(SubmitApplicationState state) {
        this.state = state;
    }

    /**
     * Registers a listener that is told when the state changes.
     * @param listener the listener to add
     */
    public void addListener(SubmitApplicationViewModelListener listener) {
        listeners.add(listener);
    }

    /**
     * Tells every listener that the state changed.
     */
    public void notifyStateChanged() {
        for (SubmitApplicationViewModelListener listener : listeners) {
            listener.stateChanged();
        }
    }
}
