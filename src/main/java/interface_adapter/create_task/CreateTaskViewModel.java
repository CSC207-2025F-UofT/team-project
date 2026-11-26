package interface_adapter.create_task;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import use_case.create_task.CreateTaskOutputData;

/** Publishes a result for every create attempt, including repeated failures. */
public class CreateTaskViewModel {
    private final PropertyChangeSupport changes = new PropertyChangeSupport(this);
    private CreateTaskOutputData createdTask;
    private String message;

    public CreateTaskOutputData getCreatedTask() {
        return createdTask;
    }

    public String getMessage() {
        return message;
    }

    void publish(CreateTaskOutputData task, String message) {
        this.createdTask = task;
        this.message = message;
        changes.firePropertyChange("createResult", null, message);
    }

    public void addListener(PropertyChangeListener listener) {
        changes.addPropertyChangeListener(listener);
    }

    public void removeListener(PropertyChangeListener listener) {
        changes.removePropertyChangeListener(listener);
    }
}