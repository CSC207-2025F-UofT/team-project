package view;

import interface_adapter.add_course.AddCourseController;
import interface_adapter.add_course.AddCourseState;
import interface_adapter.add_course.AddCourseViewModel;
import interface_adapter.add_course.AddCourseViewModelListener;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;
import java.util.List;

/**
 * The View for adding courses to the logged-in student.
 */
public class AddCourseView extends JPanel implements AddCourseViewModelListener {

    private final AddCourseViewModel addCourseViewModel;
    private final JTextField coursesInputField = new JTextField(20);
    private final JLabel messageLabel = new JLabel(" ");
    private final JLabel coursesLabel = new JLabel(" ");
    private AddCourseController addCourseController;

    public AddCourseView(AddCourseViewModel addCourseViewModel) {
        this.addCourseViewModel = addCourseViewModel;
        this.addCourseViewModel.addListener(this);

        final JLabel title = new JLabel(AddCourseViewModel.TITLE_LABEL);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        final LabelTextPanel coursesInfo = new LabelTextPanel(
                new JLabel(AddCourseViewModel.COURSES_LABEL), coursesInputField);

        final JButton addButton = new JButton(AddCourseViewModel.ADD_BUTTON_LABEL);
        addButton.addActionListener(evt -> {
            if (addCourseController != null) {
                final List<String> rawCodes = Arrays.asList(coursesInputField.getText().split(","));
                addCourseController.execute(rawCodes);
            }
        });

        final JButton backButton = new JButton("Back");
        backButton.addActionListener(evt -> {
            if (addCourseController != null) {
                addCourseController.switchToLoggedInView();
            }
        });

        final JPanel buttons = new JPanel();
        buttons.add(addButton);
        buttons.add(backButton);

        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.add(title);
        this.add(coursesInfo);
        this.add(buttons);
        this.add(messageLabel);
        this.add(coursesLabel);
    }

    @Override
    public void stateChanged() {
        final AddCourseState state = addCourseViewModel.getState();
        if (state.getError() != null) {
            messageLabel.setText(state.getError());
        }
        else {
            messageLabel.setText(state.getMessage() == null ? " " : state.getMessage());
            coursesInputField.setText("");
        }
        coursesLabel.setText("Your courses: " + String.join(", ", state.getCourses()));
    }

    public void setAddCourseController(AddCourseController addCourseController) {
        this.addCourseController = addCourseController;
    }
}
