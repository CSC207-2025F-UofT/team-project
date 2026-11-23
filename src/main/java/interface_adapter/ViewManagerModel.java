package interface_adapter;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

/**
 * Remembers which screen is showing and tells listeners when it changes.
 */
public class ViewManagerModel {
    /** Name of the property fired when the active screen changes. */
    public static final String ACTIVE_VIEW = "activeView";

    private final PropertyChangeSupport support = new PropertyChangeSupport(this);
    private String activeView;

    /**
     * Returns the name of the screen that should be showing.
     *
     * @return the active view name, or null if none was chosen yet
     */
    public String getActiveView() {
        return activeView;
    }

    /**
     * Switches to another screen and notifies listeners.
     *
     * @param viewName the name the screen was registered under
     */
    public void setActiveView(String viewName) {
        final String old = this.activeView;
        this.activeView = viewName;
        support.firePropertyChange(ACTIVE_VIEW, old, viewName);
    }

    /**
     * Registers a listener for screen changes.
     *
     * @param listener the listener to notify
     */
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        support.addPropertyChangeListener(listener);
    }
}
