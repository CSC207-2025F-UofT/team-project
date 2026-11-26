package view;

import entity.DifficultyLevel;
import interface_adapter.create_task.CreateTaskController;
import interface_adapter.create_task.CreateTaskViewModel;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dialog;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Window;
import java.beans.PropertyChangeListener;
import java.time.LocalDate;
import java.util.Optional;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import use_case.create_task.CreateTaskOutputData;

public class CreateTaskView extends JPanel {
    private final JTextField titleField = new JTextField(24);
    private final JTextArea descriptionArea = new JTextArea(3, 24);
    private final DeadlinePicker deadlinePicker = new DeadlinePicker();
    private final JTextField estimatedHoursField = new JTextField(10);
    private final JComboBox<DifficultyLevel> difficultyCombo = new JComboBox<>(DifficultyLevel.values());
    private final JButton createButton = new JButton("Create Task");
    private final JButton cancelButton = new JButton("Cancel");

    private CreateTaskOutputData createdResult;

    public CreateTaskView(int currentUserId,
                          CreateTaskController controller,
                          CreateTaskViewModel viewModel,
                          Runnable onClose) {
        super(new BorderLayout(8, 8));
        setBorder(javax.swing.BorderFactory.createEmptyBorder(12, 12, 12, 12));

        JPanel form = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4, 4, 4, 4);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        int row = 0;
        addField(form, gbc, row++, "Title", titleField);

        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.weightx = 0;
        form.add(new JLabel("Description"), gbc);
        gbc.gridx = 1;
        gbc.weightx = 1;
        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);
        form.add(new JScrollPane(descriptionArea), gbc);
        row++;

        addField(form, gbc, row++, "Deadline", deadlinePicker);
        addField(form, gbc, row++, "Estimated Hours", estimatedHoursField);
        addField(form, gbc, row++, "Difficulty", difficultyCombo);

        add(form, BorderLayout.CENTER);

        JPanel actions = new JPanel(new FlowLayout(FlowLayout.TRAILING));
        actions.add(cancelButton);
        actions.add(createButton);
        add(actions, BorderLayout.SOUTH);

        PropertyChangeListener listener = evt -> {
            if ("createResult".equals(evt.getPropertyName())) {
                CreateTaskOutputData created = viewModel.getCreatedTask();
                String msg = viewModel.getMessage();
                if (created != null) {
                    this.createdResult = created;
                    JOptionPane.showMessageDialog(this, msg, "Success", JOptionPane.INFORMATION_MESSAGE);
                    if (onClose != null) {
                        onClose.run();
                    }
                } else if (msg != null) {
                    JOptionPane.showMessageDialog(this, msg, "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        };
        viewModel.addListener(listener);

        createButton.addActionListener(evt -> {
            Double hours = parseHours(estimatedHoursField.getText());
            if (hours == null && !estimatedHoursField.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(
                        this, "Estimated hours must be a valid number.", "Input Error", JOptionPane.ERROR_MESSAGE
                );
                return;
            }
            LocalDate deadline = getSelectedDeadline();
            DifficultyLevel difficulty = (DifficultyLevel) difficultyCombo.getSelectedItem();
            controller.create(
                    currentUserId,
                    titleField.getText(),
                    descriptionArea.getText(),
                    deadline,
                    hours,
                    difficulty
            );
        });

        cancelButton.addActionListener(evt -> {
            this.createdResult = null;
            if (onClose != null) {
                onClose.run();
            }
        });
    }

    public static Optional<CreateTaskOutputData> open(Component parent,
                                                      int currentUserId,
                                                      CreateTaskController controller,
                                                      CreateTaskViewModel viewModel) {
        Window owner = parent == null ? null : SwingUtilities.getWindowAncestor(parent);
        JDialog dialog = new JDialog(owner, "Create Task", Dialog.ModalityType.APPLICATION_MODAL);
        final CreateTaskOutputData[] result = new CreateTaskOutputData[1];
        CreateTaskView view = new CreateTaskView(currentUserId, controller, viewModel, () -> {
            result[0] = view.createdResult;
            dialog.dispose();
        });
        dialog.setContentPane(view);
        dialog.pack();
        dialog.setLocationRelativeTo(parent);
        dialog.setVisible(true);
        return Optional.ofNullable(result[0]);
    }

    private void addField(JPanel form, GridBagConstraints gbc, int row, String label, Component input) {
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.weightx = 0;
        form.add(new JLabel(label), gbc);
        gbc.gridx = 1;
        gbc.weightx = 1;
        form.add(input, gbc);
    }

    private Double parseHours(String raw) {
        String trimmed = raw == null ? "" : raw.trim();
        if (trimmed.isEmpty()) {
            return null;
        }
        try {
            return Double.parseDouble(trimmed);
        } catch (NumberFormatException ex) {
            return null;
        }
    }

    private LocalDate getSelectedDeadline() {
        // Adjust if DeadlinePicker exposes a custom getter such as getSelectedDate() or getDeadline()
        return LocalDate.now();
    }
}