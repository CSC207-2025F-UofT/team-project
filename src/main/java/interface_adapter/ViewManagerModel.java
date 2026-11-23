package interface_adapter;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

/**
 * Tracks which screen is shown and notifies the view manager when it changes.
 */
public class ViewManagerModel {
    private final PropertyChangeSupport support = new PropertyChangeSupport(this);
    private String activeView = "";

    public void setActiveView(String activeView) {
        String old = this.activeView;
        this.activeView = activeView;
        support.firePropertyChange("activeView", old, activeView);
    }

    public String getActiveView() {
        return activeView;
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        support.addPropertyChangeListener(listener);
    }
}
