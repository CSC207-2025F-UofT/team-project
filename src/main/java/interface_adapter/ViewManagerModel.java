package interface_adapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Remembers which screen is showing and tells listeners when it changes. Written the same way as the other
 * view models: a state, {@link #setState(ViewManagerState)}, {@link #notifyStateChanged()} and listeners.
 */
public class ViewManagerModel {
    private final List<ViewManagerModelListener> listeners = new ArrayList<>();
    private ViewManagerState state = new ViewManagerState();

    /**
     * Returns the current state.
     *
     * @return the state
     */
    public ViewManagerState getState() {
        return state;
    }

    /**
     * Replaces the state. Call {@link #notifyStateChanged()} afterwards to tell the listeners.
     *
     * @param state the new state
     */
    public void setState(ViewManagerState state) {
        this.state = state;
    }

    /**
     * Registers a listener.
     *
     * @param listener the listener to notify
     */
    public void addListener(ViewManagerModelListener listener) {
        listeners.add(listener);
    }

    /**
     * Tells every listener the state changed.
     */
    public void notifyStateChanged() {
        for (ViewManagerModelListener listener : listeners) {
            listener.stateChanged();
        }
    }

    /**
     * Returns the name of the screen that should be showing.
     *
     * @return the active view name, or null if none was chosen yet
     */
    public String getActiveView() {
        return state.getActiveView();
    }

    /**
     * Shortcut for presenters and buttons: switches to a screen and notifies the listeners.
     *
     * @param viewName the name the screen was registered under
     */
    public void setActiveView(String viewName) {
        final ViewManagerState next = new ViewManagerState();
        next.setActiveView(viewName);
        setState(next);
        notifyStateChanged();
    }
}
