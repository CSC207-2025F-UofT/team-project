package interface_adapter.enrol_course;

import java.util.ArrayList;
import java.util.List;

/**
 * The ViewModel for the Enrol View.
 */
public class EnrolViewModel {

    public static final String VIEW_NAME = "enrol course";

    public static final String TITLE_LABEL = "Enrol in a Course";
    public static final String CODE_LABEL = "Course code";
    public static final String NAME_LABEL = "Course name";
    public static final String TARGET_GRADE_LABEL = "Target grade";
    public static final String TOPICS_LABEL = "Main topics (one per line)";
    public static final String ENROL_BUTTON_LABEL = "Enrol";
    public static final String CANCEL_BUTTON_LABEL = "Cancel";

    /** The choices offered in the target grade drop-down, as plain labels. */
    public static final String[] GRADE_OPTIONS = {
            "A+", "A", "A-", "B+", "B", "B-", "C+", "C", "C-", "D+", "D", "D-", "F",
    };

    private final List<EnrolViewModelListener> listeners = new ArrayList<>();

    private EnrolState state = new EnrolState();

    public EnrolState getState() {
        return state;
    }

    public void setState(EnrolState state) {
        this.state = state;
    }

    public void addListener(EnrolViewModelListener listener) {
        listeners.add(listener);
    }

    public void notifyStateChanged() {
        for (EnrolViewModelListener listener : listeners) {
            listener.stateChanged();
        }
    }
}
