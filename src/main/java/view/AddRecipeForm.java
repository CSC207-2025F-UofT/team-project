package view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.GridLayout;
import java.awt.BorderLayout;

public class AddRecipeForm {
    private JFrame frame;
    private JTextField recipeNameField, ingredientsField, cuisineField, cookingTimeField,
            mealTypeField, tagsField, servingSizeField;
    private JLabel recipeNameLabel, ingredientsLabel, instructionsLabel, cuisineLabel,
            cookingTimeLabel, mealTypeLabel, tagsLabel, servingSizeLabel;
    private JTextArea ingredientsArea, instructionsArea;
    private JTable ingredientsTable;
    private DefaultTableModel ingredientsModel;

    private int recipeID; // automatically generated


    public void AddRecipePopUp() {
        frame = new JFrame("Add New Recipe");
        frame.setSize(480, 600);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        //-------------- main panel ----------------
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        //--------------- top panel ----------------

        JPanel recipeInfoPanel = new JPanel(new GridLayout(7, 2, 10, 10));
        recipeNameLabel = new JLabel("Recipe Name:");
        recipeNameField = new JTextField();

        recipeInfoPanel.add(recipeNameLabel);
        recipeInfoPanel.add(recipeNameField);

        servingSizeLabel = new JLabel("Serving Size:");
        servingSizeField = new JTextField();

        recipeInfoPanel.add(servingSizeLabel);
        recipeInfoPanel.add(servingSizeField);

        cuisineLabel = new JLabel("Cuisine:");
        cuisineField = new JTextField();

        recipeInfoPanel.add(cuisineLabel);
        recipeInfoPanel.add(cuisineField);

        cookingTimeLabel = new JLabel("Cooking Time:");
        cookingTimeField = new JTextField();

        recipeInfoPanel.add(cookingTimeLabel);
        recipeInfoPanel.add(cookingTimeField);

        mealTypeLabel = new JLabel("Meal Type:");
        mealTypeField = new JTextField();

        recipeInfoPanel.add(mealTypeLabel);
        recipeInfoPanel.add(mealTypeField);

        tagsLabel = new JLabel("Tags:");
        // tooltip not working :(
        tagsLabel.setToolTipText("Enter multiple tags separated by commas.");
        tagsField = new JTextField();

        recipeInfoPanel.add(tagsLabel);
        recipeInfoPanel.add(tagsField);

        servingSizeLabel = new JLabel("Serving Size:");
        servingSizeField = new JTextField();

        recipeInfoPanel.add(servingSizeLabel);
        recipeInfoPanel.add(servingSizeField);

        //--------------ingredients panel----------------
        JPanel ingredientsPanel = new JPanel();
        ingredientsPanel.setLayout(new BorderLayout(10, 10));
        ingredientsPanel.setBorder(BorderFactory.createTitledBorder("Ingredients"));

        String[] columnNames = {"Ingredient", "Quantity", "Unit"};
        ingredientsModel = new DefaultTableModel(columnNames, 1);
        ingredientsTable = new JTable(ingredientsModel);

        JScrollPane ingredientsScrollPane = new JScrollPane(ingredientsTable);
        ingredientsPanel.add(ingredientsScrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        JButton addIngredientButton = new JButton("Add Ingredient");

        mainPanel.add(recipeInfoPanel);
        mainPanel.add(ingredientsPanel);
        frame.add(mainPanel);
        frame.setVisible(true);
    }
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            AddRecipeForm form = new AddRecipeForm();
            form.AddRecipePopUp();  // show the window
        });
    }

}