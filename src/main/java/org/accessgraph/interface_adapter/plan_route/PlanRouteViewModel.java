package org.accessgraph.interface_adapter.plan_route;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

/** Holds the state for the plan-route view and notifies it of changes (Observer pattern). */
public final class PlanRouteViewModel {
    public static final String STATE_PROPERTY = "state";
    private final PropertyChangeSupport support = new PropertyChangeSupport(this);
    private PlanRouteState state = PlanRouteState.empty();

    public PlanRouteState getState() {
        return state;
    }

    public void setState(PlanRouteState state) {
        this.state = state;
    }

    public void firePropertyChanged() {
        support.firePropertyChange(STATE_PROPERTY, null, state);
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        support.addPropertyChangeListener(listener);
    }
}
