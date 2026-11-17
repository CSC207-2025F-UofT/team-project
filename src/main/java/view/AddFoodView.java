package view;

import interface_adapter.add_food.AddFoodController;
import interface_adapter.add_food.AddFoodViewModel;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import java.awt.GridLayout;

/**
 * Swing view for the Add Food use case.
 *
 * Collects food information from the user and delegates
 * the Add Food operation to the controller.
 *
 * Observes the view model to display success or failure
 * messages after the operation is completed.
 */
public class AddFoodView extends JPanel {

    // Controller responsible for executing the use case.
    private final AddFoodController controller;

    // View model containing the result of the operation.
    private final AddFoodViewModel viewModel;

    // Input fields.
    private final JTextField nameField = new JTextField(20);
    private final JTextField caloriesField = new JTextField(20);
    private final JTextField proteinField = new JTextField(20);
    private final JTextField carbsField = new JTextField(20);
    private final JTextField fatField = new JTextField(20);

    // Displays success and error messages.
    private final JLabel messageLabel = new JLabel(" ");

    /**
     * Constructs the Add Food view.
     *
     * @param controller controller for the Add Food use case
     * @param viewModel view model that provides operation results
     * @throws IllegalArgumentException if either dependency is null
     */
    public AddFoodView(AddFoodController controller,
                       AddFoodViewModel viewModel) {

        if (controller == null || viewModel == null) {
            throw new IllegalArgumentException(
                    "Add Food view dependencies cannot be null.");
        }

        this.controller = controller;
        this.viewModel = viewModel;

        initializeUI();
        registerListeners();
    }

    /**
     * Creates and arranges the Swing components.
     */
    private void initializeUI() {

        // Use a simple grid for the initial interface.
        setLayout(new GridLayout(0, 2, 10, 10));

        add(new JLabel("Food Name:"));
        add(nameField);

        add(new JLabel("Calories (per 100g):"));
        add(caloriesField);

        add(new JLabel("Protein (g per 100g):"));
        add(proteinField);

        add(new JLabel("Carbs (g per 100g):"));
        add(carbsField);

        add(new JLabel("Fat (g per 100g):"));
        add(fatField);

        JButton addButton = new JButton("Add Food");

        // Execute the use case when the button is clicked.
        addButton.addActionListener(event -> addFood());

        add(addButton);
        add(messageLabel);
    }

    /**
     * Registers a listener to observe view model changes.
     */
    private void registerListeners() {

        viewModel.addPropertyChangeListener(event -> {

            if ("addFoodResult".equals(event.getPropertyName())) {
                updateView();
            }
        });
    }

    /**
     * Reads the user's input and passes it to the controller.
     *
     * Converts nutritional values from text into doubles.
     * Business validation remains in the use case and entities.
     */
    private void addFood() {

        try {
            String name = nameField.getText();

            double calories =
                    Double.parseDouble(caloriesField.getText().trim());

            double protein =
                    Double.parseDouble(proteinField.getText().trim());

            double carbs =
                    Double.parseDouble(carbsField.getText().trim());

            double fat =
                    Double.parseDouble(fatField.getText().trim());

            controller.execute(
                    name, calories, protein, carbs, fat
            );

        } catch (NumberFormatException exception) {
            // Text-to-number conversion is a UI concern.
            messageLabel.setText(
                    "Please enter valid numbers for nutrition.");
        }
    }

    /**
     * Refreshes the displayed result using the view model.
     */
    private void updateView() {

        messageLabel.setText(viewModel.getMessage());

        if (viewModel.isSuccess()) {
            // Clear the form after successfully adding a food.
            nameField.setText("");
            caloriesField.setText("");
            proteinField.setText("");
            carbsField.setText("");
            fatField.setText("");
        }
    }
}
