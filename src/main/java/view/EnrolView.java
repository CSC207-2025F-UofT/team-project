package view;

import interface_adapter.enrol_course.EnrolCourseController;
import interface_adapter.enrol_course.EnrolState;
import interface_adapter.enrol_course.EnrolViewModel;
import interface_adapter.enrol_course.EnrolViewModelListener;

import javax.swing.*;
import java.awt.*;

/**
 * The View for the Enrol Course Use Case: a form for the course details.
 */
public class EnrolView extends JPanel implements EnrolViewModelListener {

    private final EnrolViewModel enrolViewModel;
    private EnrolCourseController enrolCourseController;

    private final JTextField codeInputField = new JTextField(15);
    private final JTextField nameInputField = new JTextField(15);
    private final JComboBox<String> targetGradeInput = new JComboBox<>(EnrolViewModel.GRADE_OPTIONS);
    private final JTextArea topicsInputArea = new JTextArea(6, 20);

    public EnrolView(EnrolViewModel enrolViewModel) {
        this.enrolViewModel = enrolViewModel;
        this.enrolViewModel.addListener(this);

        final JLabel title = new JLabel(EnrolViewModel.TITLE_LABEL);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        final LabelTextPanel codeInfo = new LabelTextPanel(
                new JLabel(EnrolViewModel.CODE_LABEL), codeInputField);
        final LabelTextPanel nameInfo = new LabelTextPanel(
                new JLabel(EnrolViewModel.NAME_LABEL), nameInputField);

        final JPanel gradeInfo = new JPanel();
        gradeInfo.add(new JLabel(EnrolViewModel.TARGET_GRADE_LABEL));
        targetGradeInput.setSelectedItem("A");
        gradeInfo.add(targetGradeInput);

        final JPanel topicsInfo = new JPanel(new BorderLayout());
        topicsInfo.add(new JLabel(EnrolViewModel.TOPICS_LABEL), BorderLayout.NORTH);
        topicsInfo.add(new JScrollPane(topicsInputArea), BorderLayout.CENTER);

        final JPanel buttons = new JPanel();
        final JButton enrol = new JButton(EnrolViewModel.ENROL_BUTTON_LABEL);
        final JButton cancel = new JButton(EnrolViewModel.CANCEL_BUTTON_LABEL);
        buttons.add(enrol);
        buttons.add(cancel);

        enrol.addActionListener(evt -> enrolCourseController.execute(
                codeInputField.getText(),
                nameInputField.getText(),
                (String) targetGradeInput.getSelectedItem(),
                topicsInputArea.getText()));

        cancel.addActionListener(evt -> {
            clearForm();
            enrolCourseController.switchToHomeView();
        });

        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.add(title);
        this.add(codeInfo);
        this.add(nameInfo);
        this.add(gradeInfo);
        this.add(topicsInfo);
        this.add(buttons);
    }

    @Override
    public void stateChanged() {
        final EnrolState state = enrolViewModel.getState();
        if (state.getErrorMessage() != null) {
            JOptionPane.showMessageDialog(this, state.getErrorMessage());
        }
        else if (state.getEnrolledCourseCode() != null) {
            JOptionPane.showMessageDialog(this, "Enrolled in " + state.getEnrolledCourseCode() + ".");
            clearForm();
        }
    }

    private void clearForm() {
        codeInputField.setText("");
        nameInputField.setText("");
        targetGradeInput.setSelectedItem("A");
        topicsInputArea.setText("");
    }

    public void setEnrolCourseController(EnrolCourseController controller) {
        this.enrolCourseController = controller;
    }
}
