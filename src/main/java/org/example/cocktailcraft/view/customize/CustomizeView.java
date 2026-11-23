package org.example.cocktailcraft.view.customize;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import org.example.cocktailcraft.entity.RecipeIngredient;
import org.example.cocktailcraft.interface_adapter.customize.CustomizeState;
import org.example.cocktailcraft.interface_adapter.customize.CustomizeViewModel;
import org.example.cocktailcraft.interface_adapter.customize.CustomizeViewModelListener;
import org.example.cocktailcraft.interface_adapter.customize.CustomizeController;
import org.example.cocktailcraft.interface_adapter.customize.CustomizeState;

public class CustomizeView extends JPanel implements CustomizeViewModelListener {

    private final CustomizeViewModel viewModel;

    private final CustomizeController controller;

    private final Supplier<String> currentUserId;

    private final JTextField nameInputField = new JTextField(25);

    private final DefaultTableModel ingredientModel =
            new DefaultTableModel(
                    new String[] {"Ingredient", "Quantity", "Unit"},
                    0
            );

    private final JTable ingredientTable = new JTable(ingredientModel);

    private final JButton addButton = new JButton("Add Ingredient");
    private final JButton removeButton = new JButton("Remove Selected");
    private final JButton saveButton = new JButton("Save");
    private final JButton cancelButton = new JButton("Cancel");

    private final JLabel messageLabel = new JLabel(" ");

    public CustomizeView(
            CustomizeViewModel viewModel,
            CustomizeController controller,
            Supplier<String> currentUserId) {

        this.viewModel = viewModel;
        this.controller = controller;
        this.currentUserId = currentUserId;

        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JPanel header = new JPanel(new BorderLayout(10, 10));
        header.add(new JLabel("Customize Cocktail"), BorderLayout.NORTH);

        JPanel namePanel = new JPanel(new BorderLayout(10, 0));
        namePanel.add(new JLabel("Recipe name:"), BorderLayout.WEST);
        namePanel.add(nameInputField, BorderLayout.CENTER);

        header.add(namePanel, BorderLayout.CENTER);
        add(header, BorderLayout.NORTH);

        ingredientTable.setFillsViewportHeight(true);
        add(new JScrollPane(ingredientTable), BorderLayout.CENTER);

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttons.add(addButton);
        buttons.add(removeButton);
        buttons.add(cancelButton);
        buttons.add(saveButton);

        JPanel footer = new JPanel(new BorderLayout());
        footer.add(messageLabel, BorderLayout.NORTH);
        footer.add(buttons, BorderLayout.SOUTH);

        add(footer, BorderLayout.SOUTH);

        addButton.addActionListener(event -> {
            if (ingredientTable.isEditing()
                    && !ingredientTable.getCellEditor().stopCellEditing()) {
                return;
            }

            ingredientModel.addRow(new Object[] {"", "", ""});
            messageLabel.setText(" ");
        });

        removeButton.addActionListener(event -> {
            if (ingredientTable.isEditing()
                    && !ingredientTable.getCellEditor().stopCellEditing()) {
                return;
            }

            int[] selectedRows = ingredientTable.getSelectedRows();

            if (selectedRows.length == 0) {
                messageLabel.setText(
                        "Please select an ingredient to remove."
                );
                return;
            }

            for (int index = selectedRows.length - 1; index >= 0; index--) {
                ingredientModel.removeRow(selectedRows[index]);
            }

            messageLabel.setText(" ");
        });
        saveButton.addActionListener(event -> saveRecipe());

        viewModel.addListener(this);
        stateChanged();
    }

    private void saveRecipe() {
        if (ingredientTable.isEditing()
                && !ingredientTable.getCellEditor().stopCellEditing()) {
            return;
        }

        List<RecipeIngredient> ingredients = new ArrayList<>();

        for (int row = 0; row < ingredientModel.getRowCount(); row++) {
            ingredients.add(new RecipeIngredient(
                    getCellText(row, 0),
                    getCellText(row, 1),
                    getCellText(row, 2)
            ));
        }

        CustomizeState state = viewModel.getState();
        state.setName(nameInputField.getText());
        state.setIngredients(ingredients);
        state.setErrorMessage(null);
        state.setSaved(false);

        controller.execute(
                state.getCustomRecipeId(),
                currentUserId.get(),
                state.getOriginalCocktailId(),
                state.getName(),
                state.getIngredients()
        );
    }

    private String getCellText(int row, int column) {
        Object value = ingredientModel.getValueAt(row, column);

        if (value == null) {
            return "";
        }

        return value.toString();
    }

    @Override
    public void stateChanged() {
        CustomizeState state = viewModel.getState();

        if (ingredientTable.isEditing()) {
            ingredientTable.getCellEditor().cancelCellEditing();
        }

        nameInputField.setText(state.getName());

        ingredientModel.setRowCount(0);

        for (RecipeIngredient ingredient : state.getIngredients()) {
            ingredientModel.addRow(new Object[] {
                    ingredient.getName(),
                    ingredient.getAmount(),
                    ingredient.getUnit()
            });
        }

        if (state.getErrorMessage() != null) {
            messageLabel.setText(state.getErrorMessage());
        } else if (state.isSaved()) {
            messageLabel.setText("Recipe saved successfully.");
        } else {
            messageLabel.setText(" ");
        }
    }

    public void setCancelAction(Runnable action) {
        cancelButton.addActionListener(event -> {
            if (ingredientTable.isEditing()) {
                ingredientTable.getCellEditor().cancelCellEditing();
            }

            action.run();
        });
    }
}