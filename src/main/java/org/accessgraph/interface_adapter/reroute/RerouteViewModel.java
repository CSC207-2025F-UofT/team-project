package org.accessgraph.interface_adapter.reroute;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

/** Holds the state for the disruption view and notifies it of changes (Observer pattern). */
public final class RerouteViewModel {
    public static final String STATE_PROPERTY = "state";
    private final PropertyChangeSupport support = new PropertyChangeSupport(this);
    private RerouteState state = RerouteState.empty();

    public RerouteState getState() {
        return state;
    }

    public void setState(RerouteState state) {
        this.state = state;
    }

    public void firePropertyChanged() {
        support.firePropertyChange(STATE_PROPERTY, null, state);
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        support.addPropertyChangeListener(listener);
    }
}
