package interface_adapter.course_progress;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * The View Model for the Course Progress View.
 */
public class CourseProgressViewModel {

    public static final String VIEW_NAME = "course progress";

    /**
     * The target grades the user can choose from, highest first. These are the labels of
     * entity.LetterGrade; views use this list so they do not import entities.
     */
    public static final List<String> GRADE_OPTIONS = Collections.unmodifiableList(Arrays.asList(
            "A+", "A", "A-", "B+", "B", "B-", "C+", "C", "C-", "D+", "D", "D-", "F"));

    private final List<CourseProgressViewModelListener> listeners = new ArrayList<>();

    private CourseProgressState state = new CourseProgressState();

    public CourseProgressState getState() {
        return this.state;
    }

    public void setState(CourseProgressState state) {
        this.state = state;
    }

    public void addListener(CourseProgressViewModelListener listener) {
        listeners.add(listener);
    }

    public void notifyStateChanged() {
        for (CourseProgressViewModelListener listener : listeners) {
            listener.stateChanged();
        }
    }

    public void notifyTargetGradeChanged() {
        for (CourseProgressViewModelListener listener : listeners) {
            listener.targetGradeChanged();
        }
    }
}
