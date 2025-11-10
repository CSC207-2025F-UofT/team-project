import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;

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

        // Recipe Name
        JPanel recipePanel = new JPanel();
        recipePanel.setLayout(new BoxLayout(recipePanel, BoxLayout.Y_AXIS));
        recipeNameLabel = new JLabel("Recipe Name:");
        gbc.gridx = 0;
        gbc.gridy = 0;
        frame.add(recipeNameLabel, gbc);

        recipeNameField = new JTextField(20);
        gbc.gridx = 1;
        gbc.gridy = 0;
        frame.add(recipeNameField, gbc);

        // Ingredients
        ingredientsLabel = new JLabel("Ingredients:");
        gbc.gridx = 0;
        gbc.gridy = 1;
        frame.add(ingredientsLabel, gbc);

        ingredientsArea = new JTextArea(5, 20);
        JScrollPane ingredientsScroll = new JScrollPane(ingredientsArea);
        gbc.gridx = 1;
        gbc.gridy = 1;
        frame.add(ingredientsScroll, gbc);

        // Instructions
        instructionsLabel = new JLabel("Instructions:");
        gbc.gridx = 0;
        gbc.gridy = 2;
        frame.add(instructionsLabel, gbc);

        instructionsArea = new JTextArea(10, 20);
        JScrollPane instructionsScroll = new JScrollPane(instructionsArea);
        gbc.gridx = 1;
        gbc.gridy = 2;
        frame.add(instructionsScroll, gbc);

        // Cuisine
        cuisineLabel = new JLabel("Cuisine:");
        gbc.gridx = 0;
        gbc.gridy = 3;
        frame.add(cuisineLabel, gbc);

        cuisineField = new JTextField(20);
        gbc.gridx = 1;
        gbc.gridy = 3;
        frame.add(cuisineField, gbc);

        // Cooking Time
        cookingTimeLabel = new JLabel("Cooking Time (mins):");
        gbc.gridx = 0;
        gbc.gridy = 4;
        frame.add(cookingTimeLabel, gbc);

        cookingTimeField = new JTextField(20);
        gbc.gridx = 1;
        gbc.gridy = 4;
        frame.add(cookingTimeField, gbc);

        // Meal Type
        mealTypeLabel = new JLabel("Meal Type:");
        gbc.gridx = 0;

    }