package view;

import interface_adapter.meal_planning.CreatePlanController;
import interface_adapter.meal_planning.CreatePlanViewModel;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class CreatePlanView extends JPanel {

    private final CreatePlanController controller;
    private final CreatePlanViewModel viewModel;

    private final JTextField dateField =
            new JTextField(LocalDate.now().toString());
    private final JTextField slotsField =
            new JTextField("Breakfast, Lunch, Dinner");
    private final JLabel messageLabel = new JLabel(" ");

    public CreatePlanView(CreatePlanController controller,
                          CreatePlanViewModel viewModel) {
        this.controller = controller;
        this.viewModel = viewModel;

        setLayout(new BorderLayout(10, 10));

        JPanel form = new JPanel(new GridLayout(2, 2, 10, 10));
        form.add(new JLabel("Date (YYYY-MM-DD):"));
        form.add(dateField);
        form.add(new JLabel("Meal slots (separate with commas):"));
        form.add(slotsField);

        JButton createButton = new JButton("Create Plan");
        createButton.addActionListener(event -> createPlan());

        add(form, BorderLayout.NORTH);
        add(createButton, BorderLayout.CENTER);
        add(messageLabel, BorderLayout.SOUTH);

        viewModel.addPropertyChangeListener(event -> {
            if ("createPlan".equals(event.getPropertyName())) {
                updateView();
            }
        });
    }

    private void createPlan() {
        try {
            LocalDate date =
                    LocalDate.parse(dateField.getText().trim());

            String rawSlots = slotsField.getText().trim();
            List<String> slotNames = rawSlots.isEmpty()
                    ? Collections.emptyList()
                    : Arrays.asList(rawSlots.split(",", -1));

            controller.execute(date, slotNames);
        }
        catch (DateTimeParseException exception) {
            messageLabel.setText(
                    "Please enter a date as YYYY-MM-DD.");
        }
    }

    private void updateView() {
        if (!viewModel.getErrorMessage().isEmpty()) {
            messageLabel.setText(viewModel.getErrorMessage());
        }
        else {
            messageLabel.setText(
                    "Plan created for " + viewModel.getDate());
        }
    }
}