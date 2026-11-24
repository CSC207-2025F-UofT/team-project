package interface_adapter.add_course;

import java.util.ArrayList;
import java.util.List;

/**
 * The ViewModel for the Add Course View.
 */
public class AddCourseViewModel {

    public static final String VIEW_NAME = "add course";
    public static final String TITLE_LABEL = "Add Courses";
    public static final String COURSES_LABEL = "Course codes (comma separated)";
    public static final String ADD_BUTTON_LABEL = "Add";

    private final List<AddCourseViewModelListener> listeners = new ArrayList<>();

    private AddCourseState state = new AddCourseState();

    public AddCourseState getState() {
        return this.state;
    }

    public void setState(AddCourseState state) {
        this.state = state;
    }

    public void addListener(AddCourseViewModelListener listener) {
        listeners.add(listener);
    }

    public void notifyStateChanged() {
        for (AddCourseViewModelListener listener : listeners) {
            listener.stateChanged();
        }
    }
}
