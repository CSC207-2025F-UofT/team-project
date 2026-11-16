package interface_adapters.save_session;

import java.util.ArrayList;
import java.util.List;


/**
 * The View Model for the Save Session View.
 */
public class SaveSessionViewModel {
    private final List<SaveSessionViewModelListener> listeners = new ArrayList<>();
    private SaveSessionState state = new SaveSessionState();

    public SaveSessionState getState() {
        return this.state;
    }

    public void setState(SaveSessionState state) {
        this.state = state;
    }

    /**
     * Registers a listener to be notified when the Save Session View Model changes.
     * @param listener a View
     */
    public void addListener(SaveSessionViewModelListener listener) {
        listeners.add(listener);
    }

    /**
     * Notifies all listeners that the view state has changed.
     */
    public void notifyStateChanged() {
        for (SaveSessionViewModelListener listener : listeners) {
            listener.stateChanged();
        }
    }

}
