package interface_adapter;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

/**
 * Base class for every screen's ViewModel. Holds that screen's state and
 * tells the View to redraw when the state changes (Observer pattern).
 * SHARED: agree with the team before changing.
 *
 * @param <T> the type of state this ViewModel holds
 */
public class ViewModel<T> {
    private final String viewName;
    private final PropertyChangeSupport support = new PropertyChangeSupport(this);
    private T state;

    /**
     * Makes a ViewModel for one screen.
     *
     * @param viewName the screen's name (see ViewNames)
     */
    public ViewModel(String viewName) {
        this.viewName = viewName;
    }

    public String getViewName() {
        return viewName;
    }

    public T getState() {
        return state;
    }

    public void setState(T state) {
        this.state = state;
    }

    /** Tells listeners the state changed. */
    public void firePropertyChanged() {
        support.firePropertyChange("state", null, state);
    }

    /**
     * Tells listeners a specific thing changed, e.g. "error" or "success".
     *
     * @param propertyName what changed
     */
    public void firePropertyChanged(String propertyName) {
        support.firePropertyChange(propertyName, null, state);
    }

    /**
     * Adds a listener (usually the View).
     *
     * @param listener who to notify
     */
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        support.addPropertyChangeListener(listener);
    }
}
