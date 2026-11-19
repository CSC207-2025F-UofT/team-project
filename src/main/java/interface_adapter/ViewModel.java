package interface_adapter;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

/**
 * Base class for every view model: holds a state object and notifies
 * listening views when it changes (Observer pattern). Presenters call
 * setState(...) then firePropertyChanged(); views never talk to presenters.
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

    /** Tells listeners that the whole state has changed. */
    public void firePropertyChanged() {
        support.firePropertyChange("state", null, state);
    }

    /** Tells listeners that one named property has changed. */
    public void firePropertyChanged(String propertyName) {
        support.firePropertyChange(propertyName, null, state);
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        support.addPropertyChangeListener(listener);
    }
}
