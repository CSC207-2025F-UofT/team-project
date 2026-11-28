package view;

import interface_adapter.meal_planning.AddPlannedMealController;
import interface_adapter.meal_planning.AddPlannedMealViewModel;
import interface_adapter.meal_planning.AddPlannedMealViewModel.OptionItem;
import interface_adapter.edit_planned_meal.ChangePlannedPortionController;
import interface_adapter.meal_planning.AddPlannedMealViewModel.PortionItem;

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

    private final JTextField dateField =
            new JTextField(LocalDate.now().toString());
    private final JComboBox<OptionItem> slotBox = new JComboBox<>();
    private final JComboBox<OptionItem> foodBox = new JComboBox<>();
    private final JTextField gramsField = new JTextField();
    private final JLabel messageLabel = new JLabel(" ");

    private final AddPlannedMealController addController;
    private final ChangePlannedPortionController changeController;
    private final AddPlannedMealViewModel viewModel;

    private final JComboBox<PortionItem> portionBox = new JComboBox<>();
    private final JTextField newGramsField = new JTextField();
    private final JButton loadPortionsButton = new JButton("Load Planned Foods");
    private final JButton changeButton = new JButton("Change Grams");

    private boolean portionsLoaded;

    private final JButton addButton =
            new JButton("Add to Planned Meal");

    private LocalDate loadedDate;

    public AddPlannedMealView(
            AddPlannedMealController addController,
            ChangePlannedPortionController changeController,
            AddPlannedMealViewModel viewModel) {
        this.addController = addController;
        this.changeController = changeController;
        this.viewModel = viewModel;

        setLayout(new BorderLayout(10, 10));
        setBorder(new EmptyBorder(12, 12, 12, 12));

        JPanel form = new JPanel(new GridLayout(6, 2, 10, 10));
        form.add(new JLabel("Date (YYYY-MM-DD):"));
        form.add(dateField);
        form.add(new JLabel("Meal slot:"));
        form.add(slotBox);
        form.add(new JLabel("Food:"));
        form.add(foodBox);
        form.add(new JLabel("Add grams:"));
        form.add(gramsField);
        form.add(new JLabel("Planned food to edit:"));
        form.add(portionBox);
        form.add(new JLabel("New grams:"));
        form.add(newGramsField);

        JButton loadButton = new JButton("Load Options");
        loadButton.addActionListener(event -> loadOptions());
        loadPortionsButton.addActionListener(event -> loadPortions());
        changeButton.addActionListener(event -> changeGrams());
        addButton.addActionListener(event -> addMeal());

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buttons.add(loadButton);
        buttons.add(addButton);
        buttons.add(loadPortionsButton);
        buttons.add(changeButton);

        JPanel top = new JPanel();
        top.setLayout(new BoxLayout(top, BoxLayout.Y_AXIS));
        top.add(form);
        top.add(buttons);

        add(top, BorderLayout.NORTH);
        add(messageLabel, BorderLayout.SOUTH);

        slotBox.setEnabled(false);
        foodBox.setEnabled(false);
        addButton.setEnabled(false);
        portionBox.setEnabled(false);
        loadPortionsButton.setEnabled(false);
        changeButton.setEnabled(false);

        slotBox.addActionListener(event -> clearPortions());

        portionBox.addActionListener(event -> {
            PortionItem portion = (PortionItem) portionBox.getSelectedItem();
            if (portion != null) {
                newGramsField.setText(Double.toString(portion.getGrams()));
            }
        });

        viewModel.addPropertyChangeListener(event -> {
            if ("addPlannedMealOptions".equals(event.getPropertyName())) {
                showOptions();
            }
            else if ("plannedPortions".equals(event.getPropertyName())) {
                showPortions();
            }
            else if ("addPlannedMeal".equals(event.getPropertyName())) {
                messageLabel.setText(viewModel.getMessage());
                if (viewModel.isSuccess()) {
                    loadPortions();
                }
            }
        });
    }

    private void clearPortions() {
        viewModel.clearPortions();
        portionBox.removeAllItems();
        portionBox.setEnabled(false);
        changeButton.setEnabled(false);
        newGramsField.setText("");
        portionsLoaded = false;
    }

    private void loadOptions() {
        loadedDate = null;
        clearPortions();
        loadPortionsButton.setEnabled(false);
        viewModel.clearOptions();
        slotBox.removeAllItems();
        foodBox.removeAllItems();
        slotBox.setEnabled(false);
        foodBox.setEnabled(false);
        addButton.setEnabled(false);

        try {
            LocalDate date =
                    LocalDate.parse(dateField.getText().trim());
            addController.loadOptions(date);

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

        boolean hasSlots = slotBox.getItemCount() > 0;
        boolean hasFoods = foodBox.getItemCount() > 0;

        slotBox.setEnabled(hasSlots);
        foodBox.setEnabled(hasFoods);
        addButton.setEnabled(hasSlots && hasFoods);
        loadPortionsButton.setEnabled(hasSlots);

        messageLabel.setText(hasSlots && hasFoods
                ? "Select a meal slot, food, and quantity."
                : "This date needs a meal slot or a food.");
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

            addController.execute(
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

    }private void loadPortions() {
        clearPortions();

        try {
            LocalDate date = LocalDate.parse(dateField.getText().trim());
            OptionItem slot = (OptionItem) slotBox.getSelectedItem();

            if (!date.equals(loadedDate) || slot == null) {
                messageLabel.setText("Load options for this date first.");
                return;
            }

            addController.loadPortions(date, slot.getId());

            if (portionsLoaded && viewModel.getPortions().isEmpty()) {
                messageLabel.setText("No foods planned in this meal slot yet.");
            }
        }
        catch (DateTimeParseException exception) {
            messageLabel.setText("Please enter a date as YYYY-MM-DD.");
        }
    }

    private void showPortions() {
        portionsLoaded = true;

        for (PortionItem portion : viewModel.getPortions()) {
            portionBox.addItem(portion);
        }

        boolean hasPortions = portionBox.getItemCount() > 0;
        portionBox.setEnabled(hasPortions);
        changeButton.setEnabled(hasPortions);
    }

    private void changeGrams() {
        try {
            LocalDate date = LocalDate.parse(dateField.getText().trim());
            OptionItem slot = (OptionItem) slotBox.getSelectedItem();
            PortionItem portion =
                    (PortionItem) portionBox.getSelectedItem();

            if (!date.equals(loadedDate) || slot == null) {
                messageLabel.setText("Load options for this date first.");
                return;
            }
            if (portion == null) {
                messageLabel.setText("Load and select a planned food first.");
                return;
            }

            double newGrams =
                    Double.parseDouble(newGramsField.getText().trim());

            changeController.execute(
                    date, slot.getId(), portion.getIndex(), newGrams);
        }
        catch (DateTimeParseException exception) {
            messageLabel.setText("Please enter a date as YYYY-MM-DD.");
        }
        catch (NumberFormatException exception) {
            messageLabel.setText("Please enter a valid number of grams.");
        }
    }
}