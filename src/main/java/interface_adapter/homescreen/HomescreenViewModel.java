package interface_adapter.homescreen;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class HomescreenViewModel {
    private final String viewName = "homescreen";
    private HomescreenState state = new HomescreenState();
    private final PropertyChangeSupport support = new PropertyChangeSupport(this);

    public HomescreenViewModel() {}

    public void setState(HomescreenState state) {
        this.state = state;
    }

    public HomescreenState getState() {
        return state;
    }

    public String getViewName() {
        return viewName;
    }

    public void firePropertyChanged() {
        support.firePropertyChange("state", null, this.state);
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        support.addPropertyChangeListener(listener);
    }
}