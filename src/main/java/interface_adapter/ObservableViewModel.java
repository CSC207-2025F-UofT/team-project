package interface_adapter;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

/** Holds an immutable display state and notifies views when it changes on the UI thread. */
public abstract class ObservableViewModel<S> {
  private final PropertyChangeSupport changes = new PropertyChangeSupport(this);
  private S state;

  /** Starts the view model with a safe-to-render state. */
  protected ObservableViewModel(S initial) {
    state = initial;
  }

  public S getState() {
    return state;
  }

  /** Publishes one complete screen update. */
  protected final void publish(S next) {
    S previous = state;
    state = next;
    changes.firePropertyChange("state", previous, next);
  }

  /** Observes display changes for this screen's lifetime. */
  public void addPropertyChangeListener(PropertyChangeListener listener) {
    changes.addPropertyChangeListener(listener);
  }
}
