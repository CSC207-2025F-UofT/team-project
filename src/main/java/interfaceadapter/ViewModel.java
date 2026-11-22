package interfaceadapter;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

/**
 * Base class for view models. A view model holds the state shown by a view and notifies the view
 * (via property change events) when that state changes.
 *
 * @param <T> The type of state held by this view model
 */
public class ViewModel<T> {
  private final String viewName;
  private final PropertyChangeSupport support = new PropertyChangeSupport(this);
  private T state;

  /**
   * Creates a new view model.
   *
   * @param viewName Name of the view this view model belongs to
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

  /**
   * Notifies listeners that the named part of the state has changed.
   *
   * @param propertyName Name of the property that changed
   */
  public void firePropertyChange(String propertyName) {
    support.firePropertyChange(propertyName, null, state);
  }

  /**
   * Registers a listener to be notified of state changes.
   *
   * @param listener The listener to add
   */
  public void addPropertyChangeListener(PropertyChangeListener listener) {
    support.addPropertyChangeListener(listener);
  }
}
