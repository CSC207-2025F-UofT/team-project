package interface_adapter.meal_planning;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.Objects;

public class CreatePlanViewModel {

    private CreatePlanState state = new CreatePlanState();

    private final PropertyChangeSupport support =
            new PropertyChangeSupport(this);

    public CreatePlanState getState() {
        return state;
    }

    public void setState(CreatePlanState state) {
        this.state = Objects.requireNonNull(state);
    }

    public void firePropertyChanged() {
        support.firePropertyChange("state", null, state);
    }

    public void addPropertyChangeListener(
            PropertyChangeListener listener) {
        support.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(
            PropertyChangeListener listener) {
        support.removePropertyChangeListener(listener);
    }
}
