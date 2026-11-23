package interface_adapter.view_pet_details;

import java.util.ArrayList;
import java.util.List;

/**
 * The view model of the pet detail page, written the same way as the other view models in the project.
 */
public class PetDetailViewModel {
    /** Name this page is registered under in the view manager. */
    public static final String VIEW_NAME = "pet detail";

    private final List<PetDetailViewModelListener> listeners = new ArrayList<>();
    private PetDetailState state = new PetDetailState();

    /**
     * Returns the current state.
     *
     * @return the state
     */
    public PetDetailState getState() {
        return state;
    }

    /**
     * Replaces the state. Call {@link #notifyStateChanged()} afterwards to tell the view.
     *
     * @param state the new state
     */
    public void setState(PetDetailState state) {
        this.state = state;
    }

    /**
     * Registers a listener.
     *
     * @param listener the listener to notify
     */
    public void addListener(PetDetailViewModelListener listener) {
        listeners.add(listener);
    }

    /**
     * Tells every listener the state changed.
     */
    public void notifyStateChanged() {
        for (PetDetailViewModelListener listener : listeners) {
            listener.stateChanged();
        }
    }
}
