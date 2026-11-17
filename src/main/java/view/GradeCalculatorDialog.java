package view;

import interface_adapter.calculate_grade.CalculateGradeController;
import interface_adapter.calculate_grade.GradeCalculatorState;
import interface_adapter.calculate_grade.GradeCalculatorViewModel;
import interface_adapter.calculate_grade.GradeCalculatorViewModelListener;
import interface_adapter.course_progress.AssessmentRow;
import interface_adapter.course_progress.CourseProgressState;
import interface_adapter.course_progress.CourseProgressViewModel;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.WindowConstants;
import javax.swing.table.DefaultTableModel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Frame;
import java.util.ArrayList;
import java.util.List;

/**
 * The pop-up grade calculator. The student enters one row per assessment
 * (name, mark and weight) and presses Calculate. Calculating with every row empty clears
 * the course's saved marks, after the student confirms.
 */
public class GradeCalculatorDialog extends JDialog implements GradeCalculatorViewModelListener {

    private static final String[] COLUMNS = {"Assessment", "Mark (%)", "Weight (%)"};

    private final GradeCalculatorViewModel gradeCalculatorViewModel;
    private final CourseProgressViewModel courseProgressViewModel;
    private CalculateGradeController calculateGradeController;

    private final DefaultTableModel tableModel = new DefaultTableModel(COLUMNS, 0);
    private final JTable table = new JTable(tableModel);
    private final JLabel resultLabel = new JLabel(" ");
    private final JLabel errorLabel = new JLabel(" ");

    /**
     * Creates the grade calculator.
     * @param owner the main window, so the pop-up opens over it; may be null
     * @param gradeCalculatorViewModel the view model of this pop-up
     * @param courseProgressViewModel the view model of the course progress page,
     *                                which says which course is open
     */
    public GradeCalculatorDialog(Frame owner, GradeCalculatorViewModel gradeCalculatorViewModel,
                                 CourseProgressViewModel courseProgressViewModel) {
        super(owner, "Grade Calculator", true);
        this.gradeCalculatorViewModel = gradeCalculatorViewModel;
        this.courseProgressViewModel = courseProgressViewModel;
        this.gradeCalculatorViewModel.addListener(this);

        table.putClientProperty("terminateEditOnFocusLost", Boolean.TRUE);
        table.setFillsViewportHeight(true);
        final JScrollPane tableScroll = new JScrollPane(table);
        tableScroll.setPreferredSize(new Dimension(420, 180));

        final JButton addRow = new JButton("Add Row");
        final JButton removeRow = new JButton("Remove Row");
        final JButton calculate = new JButton("Calculate");
        final JButton close = new JButton("Close");

        addRow.addActionListener(evt -> tableModel.addRow(new Object[]{"", "", ""}));
        removeRow.addActionListener(evt -> removeSelectedRow());
        calculate.addActionListener(evt -> calculate());
        close.addActionListener(evt -> setVisible(false));

        final JPanel buttons = new JPanel();
        buttons.add(addRow);
        buttons.add(removeRow);
        buttons.add(calculate);
        buttons.add(close);

        errorLabel.setForeground(Color.RED);
        final JPanel messages = new JPanel();
        messages.setLayout(new BoxLayout(messages, BoxLayout.Y_AXIS));
        messages.add(resultLabel);
        messages.add(errorLabel);

        final JPanel content = new JPanel(new BorderLayout(0, 8));
        content.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        content.add(new JLabel("Enter each test, assignment or exam with its mark and weight."),
                BorderLayout.NORTH);
        content.add(tableScroll, BorderLayout.CENTER);
        final JPanel bottom = new JPanel(new BorderLayout());
        bottom.add(messages, BorderLayout.NORTH);
        bottom.add(buttons, BorderLayout.SOUTH);
        content.add(bottom, BorderLayout.SOUTH);

        setContentPane(content);
        setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
        pack();
    }

    public void setCalculateGradeController(CalculateGradeController calculateGradeController) {
        this.calculateGradeController = calculateGradeController;
    }

    /**
     * Opens the calculator for the course currently shown on the progress page,
     * filled with that course's saved assessments.
     */
    public void open() {
        final CourseProgressState progressState = courseProgressViewModel.getState();
        setTitle("Grade Calculator - " + progressState.getCourseCode());
        showRows(progressState.getAssessments());
        // Results from an earlier calculation may belong to another course.
        gradeCalculatorViewModel.setState(new GradeCalculatorState());
        resultLabel.setText(" ");
        errorLabel.setText(" ");
        setLocationRelativeTo(getOwner());
        setVisible(true);
    }

    private void removeSelectedRow() {
        final int selected = table.getSelectedRow();
        if (selected >= 0) {
            stopEditing();
            tableModel.removeRow(selected);
        }
    }

    private void calculate() {
        stopEditing();
        final List<AssessmentRow> rows = new ArrayList<>();
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            final String name = cellText(i, 0);
            final String mark = cellText(i, 1);
            final String weight = cellText(i, 2);
            // Skip rows the student left completely blank.
            if (!(name.isEmpty() && mark.isEmpty() && weight.isEmpty())) {
                rows.add(new AssessmentRow(name, mark, weight));
            }
        }
        final CourseProgressState progressState = courseProgressViewModel.getState();
        if (rows.isEmpty()) {
            if (progressState.getAssessments().isEmpty()) {
                errorLabel.setText("Enter at least one assessment.");
                return;
            }
            final int choice = JOptionPane.showConfirmDialog(this,
                    "All rows are empty. Clear every saved mark for "
                            + progressState.getCourseCode() + "?",
                    "Clear marks", JOptionPane.YES_NO_OPTION);
            if (choice != JOptionPane.YES_OPTION) {
                return;
            }
        }
        calculateGradeController.execute(
                progressState.getUsername(), progressState.getCourseCode(), rows);
    }

    private void stopEditing() {
        if (table.isEditing()) {
            table.getCellEditor().stopCellEditing();
        }
    }

    private String cellText(int row, int column) {
        final Object value = tableModel.getValueAt(row, column);
        return value == null ? "" : value.toString().trim();
    }

    private void showRows(List<AssessmentRow> rows) {
        stopEditing();
        tableModel.setRowCount(0);
        for (AssessmentRow row : rows) {
            tableModel.addRow(new Object[]{row.getName(), row.getMark(), row.getWeight()});
        }
        if (rows.isEmpty()) {
            tableModel.addRow(new Object[]{"", "", ""});
        }
    }

    /**
     * A grade calculation finished, successfully or not.
     */
    @Override
    public void stateChanged() {
        final GradeCalculatorState state = gradeCalculatorViewModel.getState();
        if (state.getCalculateError() == null) {
            showRows(courseProgressViewModel.getState().getAssessments());
            resultLabel.setText(state.getResultMessage());
            errorLabel.setText(" ");
        }
        else {
            errorLabel.setText(state.getCalculateError());
        }
    }
}
