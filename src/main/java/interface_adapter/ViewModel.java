package interface_adapter;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

/**
 * Base class for all view models. Holds a state object and notifies
 * listening views (via PropertyChangeSupport) when it changes.
 *
 * @param <T> the type of state this view model holds
 */
public class ViewModel<T> {
    private final String viewName;
    private final PropertyChangeSupport support = new PropertyChangeSupport(this);
    private T state;

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

    /** Tells listening views that the state changed. */
    public void firePropertyChanged() {
        support.firePropertyChange("state", null, state);
    }

    /** Fires a change event with a custom property name. */
    public void firePropertyChanged(String propertyName) {
        support.firePropertyChange(propertyName, null, state);
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        support.addPropertyChangeListener(listener);
    }
}
