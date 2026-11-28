package interface_adapter.create_project;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class CreateProjectViewModel {
    private final PropertyChangeSupport support =
            new PropertyChangeSupport(this);

    private String message = "";
    private boolean successful;
    private String projectName;
    private int width;
    private int height;

    public void setSuccess(
            String projectName, int width, int height) {
        this.projectName = projectName;
        this.width = width;
        this.height = height;
        this.successful = true;
        this.message = "Project created: " + projectName;

        support.firePropertyChange("result", null, message);
    }

    public void setError(String message) {
        this.successful = false;
        this.message = message;

        support.firePropertyChange("result", null, message);
    }

    public String getMessage() {
        return message;
    }

    public boolean isSuccessful() {
        return successful;
    }

    public String getProjectName() {
        return projectName;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public void addPropertyChangeListener(
            PropertyChangeListener listener) {
        support.addPropertyChangeListener(listener);
    }
}