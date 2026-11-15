package interface_adapter;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

/**
 * Tracks which view is currently shown and notifies the {@code ViewManager} when it changes.
 */
public class ViewManagerModel {
  public static final String VIEW_PROPERTY = "view";

  private final PropertyChangeSupport support = new PropertyChangeSupport(this);
  private String activeView;

  /**
   * Returns the name of the active view.
   *
   * @return the active view's name, or null if none has been shown yet
   */
  public String getActiveView() { return activeView; }

  /**
   * Switches to another view and notifies listeners.
   *
   * @param viewName the name of the view to show
   */
  public void setActiveView(String viewName) {
    final String old = this.activeView;
    this.activeView = viewName;
    support.firePropertyChange(VIEW_PROPERTY, old, viewName);
  }

  /**
   * Registers a listener for view changes.
   *
   * @param listener the listener to notify when the active view changes
   */
  public void addPropertyChangeListener(PropertyChangeListener listener) {
    support.addPropertyChangeListener(listener);
  }
}