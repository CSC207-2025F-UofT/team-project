package app;

import entity.CourseFactory;
import entity.LetterGrade;
import interface_adapter.course_progress.CourseProgressState;
import interface_adapter.course_progress.CourseProgressViewModel;
import interface_adapter.course_progress.CourseProgressViewModelListener;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import java.awt.Component;
import java.util.Arrays;

/**
 * A manual demo of use case 5 (Calculate Grade), for trying it out before the real
 * CourseProgressView exists. It wires the use case with AppBuilder, adds a sample course
 * and shows a stand-in progress page.
 * Run it from the IDE (it lives in the test sources so it never ships with the app).
 */
public final class GradeToolsDemo {

    private static final String USERNAME = "demo";
    private static final String COURSE_CODE = "CSC207";

    private GradeToolsDemo() {
    }

    /**
     * Starts the demo.
     * @param args unused
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(GradeToolsDemo::show);
    }

    private static void show() {
        final AppBuilder appBuilder = new AppBuilder().addCalculateGradeUseCase();
        appBuilder.courseDataAccessObject.saveCourse(USERNAME, new CourseFactory().create(
                COURSE_CODE, "Software Design", LetterGrade.A_MINUS,
                Arrays.asList("SOLID", "Clean Architecture", "Design Patterns")));

        // What View Course Progress (use case 3) will do when a course is opened.
        final CourseProgressViewModel viewModel = appBuilder.getCourseProgressViewModel();
        final CourseProgressState state = viewModel.getState();
        state.setUsername(USERNAME);
        state.setCourseCode(COURSE_CODE);
        state.setCourseName("Software Design");
        state.setTargetGrade("A-");
        state.setEstimatedGrade("N/A");
        state.setTargetMessage("Open the calculator to enter your marks.");

        final JLabel title = new JLabel(COURSE_CODE + " - Software Design (target A-)");
        final JLabel estimated = new JLabel();
        final JLabel targetMessage = new JLabel();
        final JButton calculate = new JButton("Calculate Grade");
        calculate.addActionListener(evt -> appBuilder.getGradeCalculatorDialog().open());
        viewModel.addListener(new CourseProgressViewModelListener() {
            @Override
            public void stateChanged() {
                estimated.setText("Estimated grade: " + viewModel.getState().getEstimatedGrade());
                targetMessage.setText(viewModel.getState().getTargetMessage());
            }

            @Override
            public void targetGradeChanged() {
                // Not used by this demo.
            }
        });

        final JPanel page = new JPanel();
        page.setLayout(new BoxLayout(page, BoxLayout.Y_AXIS));
        page.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
        for (JComponent component : new JComponent[]{title, estimated, targetMessage, calculate}) {
            component.setAlignmentX(Component.LEFT_ALIGNMENT);
            page.add(component);
        }
        viewModel.notifyStateChanged();

        // Reuse the app's main window so the calculator pop-up opens over it.
        final JFrame frame = appBuilder.build();
        frame.getContentPane().removeAll();
        frame.add(page);
        frame.setTitle("Grade tools demo");
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
