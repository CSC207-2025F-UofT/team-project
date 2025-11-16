package view;

import entities.MealType;
import interface_adapter.LogMeals.LogMealsController;
import interface_adapter.LogMeals.LogMealsViewModel;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * View for logging meals manually.
 */
public class LogMealsView extends JFrame implements PropertyChangeListener {

    private final LogMealsViewModel viewModel;
    private final LogMealsController controller;
    private final String userId;

    private JTextArea resultArea;
    private JTextField foodNameField;
    private JComboBox<MealType> mealTypeComboBox;

    public LogMealsView(LogMealsViewModel viewModel, LogMealsController controller, String userId) {
        this.viewModel = viewModel;
        this.controller = controller;
        this.userId = userId;

        viewModel.addPropertyChangeListener(this);

        initializeUI();
    }

    private void initializeUI() {
        setTitle("Log Meals - Manual Entry");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        // Top panel with input fields
        JPanel inputPanel = new JPanel(new GridLayout(3, 2, 5, 5));
        inputPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        inputPanel.add(new JLabel("Meal Type:"));
        mealTypeComboBox = new JComboBox<>(MealType.values());
        inputPanel.add(mealTypeComboBox);

        inputPanel.add(new JLabel("Food Name:"));
        foodNameField = new JTextField();
        inputPanel.add(foodNameField);

        JButton logButton = new JButton("Log Meal");
        logButton.addActionListener(e -> logMeal());
        inputPanel.add(new JLabel()); // Empty cell
        inputPanel.add(logButton);

        add(inputPanel, BorderLayout.NORTH);

        // Center panel with results
        resultArea = new JTextArea();
        resultArea.setEditable(false);
        resultArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        resultArea.setText("Enter a food name and click 'Log Meal' to fetch nutrition info from CalorieNinjas API\n\n");

        JScrollPane scrollPane = new JScrollPane(resultArea);
        add(scrollPane, BorderLayout.CENTER);

        // Allow Enter key to submit
        foodNameField.addActionListener(e -> logMeal());
    }

    private void logMeal() {
        String foodName = foodNameField.getText().trim();
        MealType mealType = (MealType) mealTypeComboBox.getSelectedItem();

        if (foodName.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a food name", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        resultArea.append("Fetching nutrition data for: " + foodName + "...\n");
        controller.logMeal(foodName, mealType, userId);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (viewModel.getSuccessMessage() != null) {
            resultArea.append("\n" + viewModel.getSuccessMessage() + "\n");
            resultArea.append("=" + "=".repeat(60) + "\n\n");
            foodNameField.setText("");
        } else if (viewModel.getErrorMessage() != null) {
            resultArea.append("ERROR: " + viewModel.getErrorMessage() + "\n\n");
            JOptionPane.showMessageDialog(this, viewModel.getErrorMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
