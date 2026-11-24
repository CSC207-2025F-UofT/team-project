package interface_adapter.add_course;

import java.util.ArrayList;

/**
 * The state for the Add Course View Model.
 */
public class AddCourseState {
    private ArrayList<String> courses = new ArrayList<>();
    private String message;
    private String error;

    public ArrayList<String> getCourses() {
        return new ArrayList<>(courses);
    }

    public void setCourses(ArrayList<String> courses) {
        this.courses = new ArrayList<>(courses);
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
