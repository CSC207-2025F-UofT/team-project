package view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class AddRecipeForm {
    private JLabel instructionsLabel;

    private int recipeID; // automatically generated


    public void AddRecipePopUp() {
        JFrame frame = new JFrame("Add New Recipe");
        frame.setSize(480, 600);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        //-------------- main panel ----------------
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        //--------------- top panel ----------------

        JPanel recipeInfoPanel = new JPanel(new GridLayout(7, 2, 10, 10));
        JLabel recipeNameLabel = new JLabel("Recipe Name:");
        JTextField recipeNameField = new JTextField();

        recipeInfoPanel.add(recipeNameLabel);
        recipeInfoPanel.add(recipeNameField);

        JLabel servingSizeLabel = new JLabel("Serving Size:");
        JTextField servingSizeField = new JTextField();

        recipeInfoPanel.add(servingSizeLabel);
        recipeInfoPanel.add(servingSizeField);

        JLabel cuisineLabel = new JLabel("Cuisine:");
        JTextField cuisineField = new JTextField();

        recipeInfoPanel.add(cuisineLabel);
        recipeInfoPanel.add(cuisineField);

        JLabel cookingTimeLabel = new JLabel("Cooking Time:");
        JTextField cookingTimeField = new JTextField();

        recipeInfoPanel.add(cookingTimeLabel);
        recipeInfoPanel.add(cookingTimeField);

        JLabel mealTypeLabel = new JLabel("Meal Type:");
        JTextField mealTypeField = new JTextField();

        recipeInfoPanel.add(mealTypeLabel);
        recipeInfoPanel.add(mealTypeField);

        JLabel tagsLabel = new JLabel("Tags:");
        // tooltip not working :(
        tagsLabel.setToolTipText("Enter multiple tags separated by commas.");
        JTextField tagsField = new JTextField();

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
        DefaultTableModel ingredientsModel = new DefaultTableModel(columnNames, 1);
        JTable ingredientsTable = new JTable(ingredientsModel);

        JScrollPane ingredientsScrollPane = new JScrollPane(ingredientsTable);
        ingredientsPanel.add(ingredientsScrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        JButton addIngredientButton = new JButton("Add Ingredient");

        mainPanel.add(recipeInfoPanel);
        mainPanel.add(ingredientsPanel);
        frame.add(mainPanel);
        frame.setVisible(true);

        // -------------- instructions panel ----------------
        JPanel instructionsPanel = new JPanel(new BorderLayout(10, 10));
        instructionsPanel.setBorder(BorderFactory.createTitledBorder("Instructions"));

        JTextArea instructionsTextArea = new JTextArea(6, 20);
        instructionsTextArea.setLineWrap(true);
        instructionsTextArea.setWrapStyleWord(true);

        JScrollPane instructionsScrollPane = new JScrollPane(instructionsTextArea,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER
        );

        instructionsPanel.add(instructionsScrollPane, BorderLayout.CENTER);

        mainPanel.add(instructionsPanel);


        // -------------- submit button panel ----------------
        JPanel bottomButtonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));

        JButton addButton = new JButton("Add");
        JButton cancelButton = new JButton("Cancel");

        bottomButtonPanel.add(addButton);
        bottomButtonPanel.add(cancelButton);

        mainPanel.add(bottomButtonPanel);
    }
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            AddRecipeForm form = new AddRecipeForm();
            form.AddRecipePopUp();  // show the window
        });
    }

}