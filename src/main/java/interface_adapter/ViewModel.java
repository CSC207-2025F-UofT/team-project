package interface_adapter;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

/**
 * Base class for view models. A presenter calls setState(...) then firePropertyChanged(),
 * and any view listening gets propertyChange(...) with the new state.
 * Each use case's view model can extend this, e.g. SubmitGuessViewModel extends ViewModel<SubmitGuessState>.
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

    /** Tells listeners the state changed (property name "state"). */
    public void firePropertyChanged() {
        support.firePropertyChange("state", null, state);
    }

    /** Tells listeners a specific property changed, e.g. "error". */
    public void firePropertyChanged(String propertyName) {
        support.firePropertyChange(propertyName, null, state);
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        support.addPropertyChangeListener(listener);
    }
}
