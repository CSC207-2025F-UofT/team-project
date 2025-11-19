package interface_adapter.clicking;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;


public class ClickingViewModel {
    public final String viewName = "clicking";
    private final ClickingState state = new ClickingState();
    private final PropertyChangeSupport support = new PropertyChangeSupport(this);

    public ClickingState getState() {
        return state;
    }

    public String getViewName() {
        return viewName;
    }

    public void firePropertyChange() {
        support.firePropertyChange("state", null, state);
    }

    public void firePropertyChange(String propertyName) {
        support.firePropertyChange(propertyName, null, state);
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        support.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        support.removePropertyChangeListener(listener);
    }
}