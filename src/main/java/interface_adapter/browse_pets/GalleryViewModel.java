package interface_adapter.browse_pets;

import java.util.ArrayList;
import java.util.List;

/**
 * The view model of the gallery, written the same way as the other view models in the project.
 */
public class GalleryViewModel {
    /** Name the gallery screen is registered under in the view manager. */
    public static final String VIEW_NAME = "gallery";

    private final List<GalleryViewModelListener> listeners = new ArrayList<>();
    private GalleryState state = new GalleryState();

    /**
     * Returns the current state.
     *
     * @return the state
     */
    public GalleryState getState() {
        return state;
    }

    /**
     * Replaces the state. Call {@link #notifyStateChanged()} afterwards to tell the view.
     *
     * @param state the new state
     */
    public void setState(GalleryState state) {
        this.state = state;
    }

    /**
     * Registers a listener.
     *
     * @param listener the listener to notify
     */
    public void addListener(GalleryViewModelListener listener) {
        listeners.add(listener);
    }

    /**
     * Tells every listener the state changed.
     */
    public void notifyStateChanged() {
        for (GalleryViewModelListener listener : listeners) {
            listener.stateChanged();
        }
    }
}
