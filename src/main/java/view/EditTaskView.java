package view;

import interface_adapter.edit_task.EditTaskController;
import interface_adapter.edit_task.EditTaskViewModel;
import use_case.edit_task.EditTaskOutputData;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeListener;
import java.time.LocalDate;
import java.util.Optional;

/** The form owns the draft. Only Save passes values to the edit use case. */
public class EditTaskView extends JPanel {
    private final EditTaskViewModel editTaskViewModel;
    private final JTextField title = new JTextField(28);
    private final JTextArea description = new JTextArea(4, 28);
    private final JTextField hours = new JTextField();
    private final JTextField progress = new JTextField();
    private final JComboBox<String> difficulty = new JComboBox<>(new String[]{"Easy", "Average", "Difficult"});
    private final DeadlinePicker deadline;
    private final JButton save = new JButton("Save");
    private final JButton cancel = new JButton("Cancel");
    private EditTaskOutputData savedTask;

    private EditTaskView(EditTaskOutputData task, EditTaskViewModel viewModel) {
        super(new GridBagLayout());
        this.editTaskViewModel = viewModel;
        setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
        // sets the title and description.
        title.setText(task.getTitle());
        description.setText(task.getDescription());
        description.setLineWrap(true);
        description.setWrapStyleWord(true);

        hours.setText(Double.toString(task.getEstimatedHoursToCompleteTask()));
        progress.setText(Integer.toString(task.getCompletionProgress()));

        int level = task.getDifficultyLevel();
        difficulty.setSelectedIndex(level >= 1 && level <= 3 ? level - 1 : -1);
        deadline = new DeadlinePicker(task.getDeadline());

        // addes the rows of all fields into the panel.
        addRow(0, "Title", title);
        addRow(1, "Description", new JScrollPane(description));
        addRow(2, "Estimated hours", hours);
        addRow(3, "Completion (%)", progress);
        addRow(4, "Difficulty", difficulty);
        addRow(5, "Deadline", deadline);

        JPanel actions = new JPanel(new FlowLayout(FlowLayout.TRAILING));
        //
        actions.add(cancel);
        actions.add(save);
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridy = 6;
        constraints.gridwidth = 2;
        constraints.anchor = GridBagConstraints.LINE_END;
        add(actions, constraints);
    }

    /** Opens a standalone layout preview with sample data. */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            EditTaskOutputData sample = new EditTaskOutputData(1, 1, "Finish CSC207 MVP",
                    "Review the edit flow", LocalDate.now().plusDays(7), 10.0, 20, (byte) 2);
            EditTaskView panel = new EditTaskView(sample, new EditTaskViewModel());
            JDialog preview = new JDialog((Window) null, "Edit Task — Layout Preview");
            preview.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
            panel.save.setEnabled(false);
            panel.save.setToolTipText("Run app.main to save edits to a sample task.");
            panel.cancel.addActionListener(event -> panel.confirmCancel(preview));
            preview.setContentPane(panel);
            preview.pack();
            preview.setLocationRelativeTo(null);
            preview.setVisible(true);
        });
    }

    private void addRow(int row, String caption, JComponent field) {
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridy = row;
        constraints.gridx = 0;
        constraints.anchor = GridBagConstraints.FIRST_LINE_START;
        constraints.insets = new Insets(4, 5, 4, 5);
        JLabel label = new JLabel(caption);
        label.setLabelFor(field instanceof JScrollPane ? description : field);
        add(label, constraints);
        constraints.gridx = 1;
        constraints.weightx = 1;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        add(field, constraints);
    }

    /** Returns the saved snapshot, or an empty result when the student discards the draft. */
    public static Optional<EditTaskOutputData> open(Component parent, int currentUserId,
            EditTaskOutputData task, EditTaskController controller, EditTaskViewModel viewModel) {
        EditTaskView form = new EditTaskView(task, viewModel);
        JDialog dialog = new JDialog(SwingUtilities.getWindowAncestor(parent), "Edit Task",
                Dialog.ModalityType.APPLICATION_MODAL);
        dialog.setContentPane(form);
        dialog.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        dialog.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent event) {
                form.confirmCancel(dialog);
            }
        });
        form.cancel.addActionListener(event -> form.confirmCancel(dialog));
        form.save.addActionListener(event -> form.submit(controller, task.getUniqueTaskId(), currentUserId));
        dialog.getRootPane().setDefaultButton(form.save);
        dialog.pack();
        dialog.setLocationRelativeTo(parent);

        PropertyChangeListener listener = event -> form.showSaveResult(dialog);
        viewModel.addListener(listener);
        try {
            dialog.setVisible(true);
        } finally {
            viewModel.removeListener(listener);
            dialog.dispose();
        }
        return Optional.ofNullable(form.savedTask);
    }

    /*
     * Updates the View with the saved results.
     */
    private void showSaveResult(JDialog dialog) {
        EditTaskOutputData result = editTaskViewModel.getSavedTask();
        if (result == null) {
            showError(editTaskViewModel.getMessage());
        } else {
            savedTask = result;
            JOptionPane.showMessageDialog(dialog, editTaskViewModel.getMessage(), "Edit Task",
                    JOptionPane.INFORMATION_MESSAGE);
            dialog.dispose();
        }
    }

    private void submit(EditTaskController controller, int taskId, int userId) {
        double estimatedHours;
        int completion;
        try {
            estimatedHours = Double.parseDouble(hours.getText().trim());
        } catch (NumberFormatException ex) {
            showError("Enter a number for estimated hours.");
            hours.requestFocusInWindow();
            return;
        }
        try {
            completion = Integer.parseInt(progress.getText().trim());
        } catch (NumberFormatException ex) {
            showError("Enter a whole number for completion progress.");
            progress.requestFocusInWindow();
            return;
        }
        controller.save(taskId, userId, title.getText(), description.getText(), deadline.getDate(),
                estimatedHours, completion, (byte) (difficulty.getSelectedIndex() + 1));
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Task could not be saved", JOptionPane.ERROR_MESSAGE);
    }

    private void confirmCancel(JDialog dialog) {
        int choice = JOptionPane.showConfirmDialog(dialog, "Discard your changes?", "Cancel editing",
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (choice == JOptionPane.YES_OPTION) {
            dialog.dispose();
        }
    }
}
