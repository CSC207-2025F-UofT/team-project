package interface_adapter.filter_search;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class FilterSearchViewModel {

    private final PropertyChangeSupport pcs = new PropertyChangeSupport(this);

    private FilterSearchState state = new FilterSearchState();

    public void setState(FilterSearchState state) {
        this.state = state;
    }

    public FilterSearchState getState() {
        return state;
    }

    public void firePropertyChanged() {
        pcs.firePropertyChange("filterSearch", null, state);
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        pcs.addPropertyChangeListener(listener);
    }
}
