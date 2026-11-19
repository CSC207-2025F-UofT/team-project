package interface_adapter.track_applications;

import java.util.ArrayList;
import java.util.List;

/**
 * The View Model for the My Applications View.
 */
public class TrackApplicationsViewModel {

    public static final String VIEW_NAME = "my applications";

    private final List<TrackApplicationsViewModelListener> listeners = new ArrayList<>();

    private TrackApplicationsState state = new TrackApplicationsState();

    public TrackApplicationsState getState() {
        return this.state;
    }

    public void setState(TrackApplicationsState state) {
        this.state = state;
    }

    /**
     * Registers a listener that is told when the state changes.
     * @param listener the listener to add
     */
    public void addListener(TrackApplicationsViewModelListener listener) {
        listeners.add(listener);
    }

    /**
     * Tells every listener that the state changed.
     */
    public void notifyStateChanged() {
        for (TrackApplicationsViewModelListener listener : listeners) {
            listener.stateChanged();
        }
    }
}
