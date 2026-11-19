package interface_adapter.phase;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

// holds the PhaseState; STATE = new portrait or error, VIEW = same one panned / zoomed / traced / pinned
public class PhaseViewModel {
    public static final String STATE_PROPERTY = "phaseState";
    public static final String VIEW_PROPERTY = "phaseView";

    private final PropertyChangeSupport support = new PropertyChangeSupport(this);
    private PhaseState state = new PhaseState();

    public PhaseState getState() {
        return state;
    }

    public void setState(PhaseState state) {
        this.state = state;
    }

    public void firePropertyChanged() {
        support.firePropertyChange(STATE_PROPERTY, null, state);
    }

    public void fireViewChanged() {
        support.firePropertyChange(VIEW_PROPERTY, null, state);
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        support.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        support.removePropertyChangeListener(listener);
    }
}
