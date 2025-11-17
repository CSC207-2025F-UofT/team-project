package interface_adapter.tasks;

import interface_adapter.tasks.dto.CourseDTO;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.List;

public class TasksViewModel {

    private List<CourseDTO> courses;
    private String error;

    private final PropertyChangeSupport pcs;

    public TasksViewModel() {
        pcs = new PropertyChangeSupport(this);
    }

    // Courses
    public List<CourseDTO> getCourses() {
        return courses;
    }

    public void setCourses(List<CourseDTO> courses) {
        List<CourseDTO> old = this.courses;
        this.courses = courses;
        pcs.firePropertyChange("courses", old, courses);
    }

    // Error
    public String getError() {
        return error;
    }

    public void setError(String error) {
        String old = this.error;
        this.error = error;
        pcs.firePropertyChange("error", old, error);
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        pcs.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        pcs.removePropertyChangeListener(listener);
    }
}
