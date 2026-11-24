package view;

import interface_adapter.meal_planning.AddPlannedMealController;
import interface_adapter.meal_planning.AddPlannedMealViewModel;
import interface_adapter.meal_planning.AddPlannedMealViewModel.OptionItem;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class AddPlannedMealView extends JPanel {

    private final AddPlannedMealController controller;
    private final AddPlannedMealViewModel viewModel;

    private final JTextField dateField =
            new JTextField(LocalDate.now().toString());
    private final JComboBox<OptionItem> slotBox = new JComboBox<>();
    private final JComboBox<OptionItem> foodBox = new JComboBox<>();
    private final JTextField gramsField = new JTextField();
    private final JLabel messageLabel = new JLabel(" ");

    private final JButton addButton =
            new JButton("Add to Planned Meal");

    private LocalDate loadedDate;

    public AddPlannedMealView(
            AddPlannedMealController controller,
            AddPlannedMealViewModel viewModel) {
        this.controller = controller;
        this.viewModel = viewModel;

        setLayout(new BorderLayout(10, 10));
        setBorder(new EmptyBorder(12, 12, 12, 12));

        JPanel form = new JPanel(new GridLayout(4, 2, 10, 10));
        form.add(new JLabel("Date (YYYY-MM-DD):"));
        form.add(dateField);
        form.add(new JLabel("Meal slot:"));
        form.add(slotBox);
        form.add(new JLabel("Food:"));
        form.add(foodBox);
        form.add(new JLabel("Grams:"));
        form.add(gramsField);

        JButton loadButton = new JButton("Load Options");
        loadButton.addActionListener(event -> loadOptions());
        addButton.addActionListener(event -> addMeal());

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buttons.add(loadButton);
        buttons.add(addButton);

        JPanel top = new JPanel();
        top.setLayout(new BoxLayout(top, BoxLayout.Y_AXIS));
        top.add(form);
        top.add(buttons);

        add(top, BorderLayout.NORTH);
        add(messageLabel, BorderLayout.SOUTH);

        slotBox.setEnabled(false);
        foodBox.setEnabled(false);
        addButton.setEnabled(false);

        viewModel.addPropertyChangeListener(event -> {
            if ("addPlannedMealOptions".equals(
                    event.getPropertyName())) {
                showOptions();
            }
            else if ("addPlannedMeal".equals(
                    event.getPropertyName())) {
                messageLabel.setText(viewModel.getMessage());
            }
        });
    }

    private void loadOptions() {
        loadedDate = null;
        viewModel.clearOptions();
        slotBox.removeAllItems();
        foodBox.removeAllItems();
        slotBox.setEnabled(false);
        foodBox.setEnabled(false);
        addButton.setEnabled(false);

        try {
            LocalDate date =
                    LocalDate.parse(dateField.getText().trim());
            controller.loadOptions(date);

            // The presenter updates the ViewModel during this call.
            if (viewModel.getMessage().isEmpty()) {
                loadedDate = date;
            }
        }
        catch (DateTimeParseException exception) {
            messageLabel.setText(
                    "Please enter a date as YYYY-MM-DD.");
        }
    }

    private void showOptions() {
        for (OptionItem slot : viewModel.getMealSlots()) {
            slotBox.addItem(slot);
        }
        for (OptionItem food : viewModel.getFoods()) {
            foodBox.addItem(food);
        }

        boolean ready = slotBox.getItemCount() > 0
                && foodBox.getItemCount() > 0;
        slotBox.setEnabled(ready);
        foodBox.setEnabled(ready);
        addButton.setEnabled(ready);

        messageLabel.setText(ready
                ? "Select a meal slot, food, and quantity."
                : "This date needs a meal slot and a food.");
    }

    private void addMeal() {
        try {
            LocalDate date =
                    LocalDate.parse(dateField.getText().trim());

            if (!date.equals(loadedDate)) {
                messageLabel.setText(
                        "Load options for this date first.");
                return;
            }

            OptionItem slot =
                    (OptionItem) slotBox.getSelectedItem();
            OptionItem food =
                    (OptionItem) foodBox.getSelectedItem();

            if (slot == null || food == null) {
                messageLabel.setText(
                        "Please select a meal slot and food.");
                return;
            }

            double grams =
                    Double.parseDouble(gramsField.getText().trim());

            controller.execute(
                    date, slot.getId(), food.getId(), grams);
        }
        catch (DateTimeParseException exception) {
            messageLabel.setText(
                    "Please enter a date as YYYY-MM-DD.");
        }
        catch (NumberFormatException exception) {
            messageLabel.setText(
                    "Please enter a valid number of grams.");
        }
    }
}