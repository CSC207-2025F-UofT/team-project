package interface_adapter.edit_task;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import use_case.edit_task.EditTaskOutputData;

/** Publishes a result for every save attempt, including repeated failures. */
public class EditTaskViewModel {
    private final PropertyChangeSupport changes = new PropertyChangeSupport(this);
    private EditTaskOutputData savedTask;
    private String message;

    public EditTaskOutputData getSavedTask() {
        return savedTask;
    }

    public String getMessage() {
        return message;
    }

    void publish(EditTaskOutputData task, String message) {
        this.savedTask = task;
        this.message = message;
        changes.firePropertyChange("saveResult", null, message);
    }

    public void addListener(PropertyChangeListener listener) {
        changes.addPropertyChangeListener(listener);
    }

    public void removeListener(PropertyChangeListener listener) {
        changes.removePropertyChangeListener(listener);
    }
}
