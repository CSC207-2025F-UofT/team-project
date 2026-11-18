package interface_adapter.submit_application;

import java.util.ArrayList;
import java.util.List;

/**
 * The View Model for the Submit Application View.
 */
public class SubmitApplicationViewModel {

    public static final String VIEW_NAME = "submit application";

    private final List<SubmitApplicationViewModelListener> listeners = new ArrayList<>();

    private SubmitApplicationState state = new SubmitApplicationState();

    public SubmitApplicationState getState() {
        return this.state;
    }

    public void setState(SubmitApplicationState state) {
        this.state = state;
    }

    public void addListener(SubmitApplicationViewModelListener listener) {
        listeners.add(listener);
    }

    public void notifyStateChanged() {
        for (SubmitApplicationViewModelListener listener : listeners) {
            listener.stateChanged();
        }
    }
}
