package interface_adapter;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

/**
 * Remembers which screen is showing and tells listeners when it changes.
 * A presenter can either call {@link #setActiveView(String)}, or call {@link #setState(String)} followed by
 * {@link #notifyViewChanged()}.
 */
public class ViewManagerModel {
    /** Name of the property fired when the active screen changes. */
    public static final String ACTIVE_VIEW = "activeView";

    private final PropertyChangeSupport support = new PropertyChangeSupport(this);
    private String activeView;
    private String announcedView;

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
        setState(viewName);
        notifyViewChanged();
    }

    /**
     * Chooses the next screen without telling listeners yet.
     *
     * @param viewName the name the screen was registered under
     */
    public void setState(String viewName) {
        this.activeView = viewName;
    }

    /**
     * Tells listeners which screen is now active. Does nothing if it did not change.
     */
    public void notifyViewChanged() {
        final String old = this.announcedView;
        this.announcedView = activeView;
        support.firePropertyChange(ACTIVE_VIEW, old, activeView);
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
