package interface_adapter;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

/**
 * Display state for one view. Presenters replace the state, and observing views re-render from it. Listeners run
 * on the presenter's thread, so Swing views must move rendering onto the event dispatch thread.
 *
 * @param <S> the immutable state type
 */
public class ViewModel<S> {
    public static final String STATE_PROPERTY = "state";

    private final String viewName;
    private final PropertyChangeSupport support = new PropertyChangeSupport(this);
    private volatile S state;

    public ViewModel(String viewName, S initialState) {
        this.viewName = viewName;
        this.state = initialState;
    }

    public String getViewName() {
        return viewName;
    }

    public S getState() {
        return state;
    }

    /** Notifies listeners even when the new state equals the old one, so a repeated error is shown again. */
    public void setState(S newState) {
        state = newState;
        support.firePropertyChange(STATE_PROPERTY, null, newState);
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        support.addPropertyChangeListener(listener);
    }
}
