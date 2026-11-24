package interface_adapter.plan_editor;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class PlanEditorViewModel {
    public static final String VIEW_NAME = "plan editor";
    private PlanEditorState state = new PlanEditorState();
    private final PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);

    public PlanEditorState getState() {
        return state;
    }

    public void setState(PlanEditorState state) {
        this.state = state;
    }

    public void firePropertyChanged() {
        propertyChangeSupport.firePropertyChange("state", null, state);
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeSupport.addPropertyChangeListener(listener);
    }
}
