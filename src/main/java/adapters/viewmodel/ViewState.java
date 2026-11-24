package adapters.viewmodel;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.Objects;

/** Publishes presentation snapshots to listeners on the publishing thread. */
public final class ViewState<T> {
    public static final String STATE_PROPERTY = "state";

    private final PropertyChangeSupport changes = new PropertyChangeSupport(this);
    private volatile T state;

    public ViewState(T initialState) {
        state = Objects.requireNonNull(initialState);
    }

    public T getState() {
        return state;
    }

    public void setState(T nextState) {
        T previous = state;
        state = Objects.requireNonNull(nextState);
        changes.firePropertyChange(STATE_PROPERTY, previous, nextState);
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        changes.addPropertyChangeListener(STATE_PROPERTY, listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        changes.removePropertyChangeListener(STATE_PROPERTY, listener);
    }
}
