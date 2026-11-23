package interface_adapter.challenge;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.List;

/** Plain UI state; listeners choose their own UI-thread dispatch mechanism. */
public final class ChallengeViewModel {
    public enum Screen { COURSES, CONFIRM, QUESTION, FEEDBACK, RESULT, LEFT }

    public static final class CourseItem {
        public final String id;
        public final String label;
        public CourseItem(String id, String label) { this.id = id; this.label = label; }
        @Override public String toString() { return label; }
    }

    public static final class State {
        public final Screen screen;
        public final String energy;
        public final List<CourseItem> courses;
        public final String courseId;
        public final String courseName;
        public final String attemptId;
        public final int questionIndex;
        public final int questionCount;
        public final String instructions;
        public final String question;
        public final List<String> options;
        public final String feedback;
        public final String result;
        public final String error;

        public State(Screen screen, String energy, List<CourseItem> courses, String courseId,
                     String courseName, String attemptId, int questionIndex, int questionCount,
                     String instructions, String question, List<String> options, String feedback,
                     String result, String error) {
            this.screen = screen;
            this.energy = energy;
            this.courses = List.copyOf(courses);
            this.courseId = courseId;
            this.courseName = courseName;
            this.attemptId = attemptId;
            this.questionIndex = questionIndex;
            this.questionCount = questionCount;
            this.instructions = instructions;
            this.question = question;
            this.options = List.copyOf(options);
            this.feedback = feedback;
            this.result = result;
            this.error = error;
        }

        State withError(String message) {
            return new State(screen, energy, courses, courseId, courseName, attemptId, questionIndex,
                    questionCount, instructions, question, options, feedback, result, message);
        }
    }

    private final PropertyChangeSupport changes = new PropertyChangeSupport(this);
    private volatile State state = new State(Screen.COURSES, "Energy: —", List.of(), "", "", "",
            0, 3, "", "", List.of(), "", "", "");

    public State getState() { return state; }
    public void addListener(PropertyChangeListener listener) { changes.addPropertyChangeListener(listener); }

    void setState(State next) {
        State previous = state;
        state = next;
        changes.firePropertyChange("state", previous, next);
    }
}
