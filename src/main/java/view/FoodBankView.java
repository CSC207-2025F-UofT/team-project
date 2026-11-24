package view;

import entity.FoodItem;
import entity.NutritionTotals;
import interface_adapter.view_food_bank.ViewFoodBankController;
import interface_adapter.view_food_bank.ViewFoodBankViewModel;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.util.List;

/**
 * Swing view for the View Food Bank use case.
 *
 * Displays food items and their nutritional information
 * in a table.
 *
 * Delegates food retrieval requests to the controller
 * and observes the ViewModel for result updates.
 *
 * This class does not directly access the food repository
 * or execute business logic.
 */
public class FoodBankView extends JPanel {

    // Controller responsible for retrieving the food bank.
    private final ViewFoodBankController controller;

    // ViewModel containing foods and retrieval status.
    private final ViewFoodBankViewModel viewModel;

    // Table model used to display food information.
    private final DefaultTableModel tableModel;

    // Table containing food entries.
    private final JTable foodTable;

    // Label displaying the current retrieval status.
    private final JLabel messageLabel;

    /**
     * Constructs the Food Bank view.
     *
     * @param controller controller for the View Food Bank use case
     * @param viewModel ViewModel providing food bank results
     * @throws IllegalArgumentException if a dependency is null
     */
    public FoodBankView(
            ViewFoodBankController controller,
            ViewFoodBankViewModel viewModel) {

        if (controller == null || viewModel == null) {
            throw new IllegalArgumentException(
                    "Food Bank view dependencies cannot be null.");
        }

        this.controller = controller;
        this.viewModel = viewModel;

        // Define the columns displayed in the food bank.
        String[] columns = {
                "Food Name",
                "Calories (kcal)",
                "Protein (g)",
                "Carbs (g)",
                "Fat (g)"
        };

        // Prevent users from editing food entries directly in the table.
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        foodTable = new JTable(tableModel);
        messageLabel = new JLabel(" ");

        initializeUI();
        registerListeners();
    }

    /**
     * Creates and arranges the Swing components.
     */
    private void initializeUI() {

        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Display the title at the top of the panel.
        JLabel titleLabel = new JLabel("Food Bank");
        add(titleLabel, BorderLayout.NORTH);

        // Allow the table to scroll when many foods are present.
        JScrollPane scrollPane = new JScrollPane(foodTable);
        add(scrollPane, BorderLayout.CENTER);

        // Create the bottom panel containing status and controls.
        JPanel bottomPanel = new JPanel(new BorderLayout());

        bottomPanel.add(messageLabel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        JButton refreshButton = new JButton("Refresh");

        // Delegate refresh requests to the controller.
        refreshButton.addActionListener(event -> refreshFoodBank());

        buttonPanel.add(refreshButton);
        bottomPanel.add(buttonPanel, BorderLayout.EAST);

        add(bottomPanel, BorderLayout.SOUTH);
    }

    /**
     * Registers a listener for ViewModel updates.
     *
     * When the presenter updates the ViewModel, the food
     * table and status message are refreshed accordingly.
     */
    private void registerListeners() {

        viewModel.addPropertyChangeListener(event -> {

            if (ViewFoodBankViewModel.FOOD_BANK_PROPERTY
                    .equals(event.getPropertyName())) {

                updateView();
            }
        });
    }

    /**
     * Requests the latest food bank data through the controller.
     *
     * Called when the user clicks the Refresh button.
     */
    public void refreshFoodBank() {
        controller.execute();
    }

    /**
     * Updates the displayed food table and status message
     * using the latest information from the ViewModel.
     */
    private void updateView() {

        // Remove previously displayed food entries.
        tableModel.setRowCount(0);

        // Display the current result message.
        messageLabel.setText(viewModel.getMessage());

        // Do not populate the table if retrieval failed.
        if (!viewModel.isSuccess()) {
            return;
        }

        List<FoodItem> foods = viewModel.getFoods();

        for (FoodItem food : foods) {

            NutritionTotals nutrition = food.getNutritionPer100g();

            Object[] row = {
                    food.getName(),
                    nutrition.getCalories(),
                    nutrition.getProtein(),
                    nutrition.getCarbs(),
                    nutrition.getFat()
            };

            tableModel.addRow(row);
        }
    }
}