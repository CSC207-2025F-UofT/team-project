package interface_adapter.graph;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

// holds the GraphState; STATE = new graph or error, VIEW = same graph panned / zoomed / traced
public class GraphViewModel {
    public static final String STATE_PROPERTY = "graphState";
    public static final String VIEW_PROPERTY = "graphView";

    private final PropertyChangeSupport support = new PropertyChangeSupport(this);
    private GraphState state = new GraphState();

    public GraphState getState() {
        return state;
    }

    public void setState(GraphState state) {
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
