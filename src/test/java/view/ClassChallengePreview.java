package view;

import app.ClassChallengeFactory;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import support.FakeChallengeState;
import use_case.challenge.ChallengeCourse;
import use_case.question.QuestionTopic;

/** UI preview with test-only enrolment/state and the REAL question API, not an integrated game. */
public final class ClassChallengePreview {
    private ClassChallengePreview() { }

    public static void main(String[] args) {
        String major = args.length == 0 ? "cs" : args[0];
        FakeChallengeState state = new FakeChallengeState();
        switch (major) {
            case "cs":
                assign(state, "preview-programming", "Programming Foundation", QuestionTopic.PROGRAMMING_FOUNDATION);
                assign(state, "preview-design", "Software Design", QuestionTopic.SOFTWARE_DESIGN);
                break;
            case "biology":
                assign(state, "preview-cell", "Cell Biology", QuestionTopic.CELL_BIOLOGY);
                assign(state, "preview-ecology", "Ecology", QuestionTopic.ECOLOGY);
                break;
            case "history":
                assign(state, "preview-world", "World History", QuestionTopic.WORLD_HISTORY);
                assign(state, "preview-perspective", "Historical Perspective", QuestionTopic.HISTORICAL_PERSPECTIVE);
                break;
            default: throw new IllegalArgumentException("Choose cs, biology or history");
        }
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("UC2 preview — test state, live Hugging Face API");
            ClassChallengeView view = ClassChallengeFactory.create(state, frame::dispose);
            frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
            frame.addWindowListener(new WindowAdapter() {
                @Override public void windowClosing(WindowEvent event) { view.requestLeave(); }
            });
            frame.setContentPane(view);
            frame.setSize(1000, 760);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }

    private static void assign(FakeChallengeState state, String id, String name, QuestionTopic topic) {
        state.assign(new ChallengeCourse(id, name, topic, null));
    }
}
