package org.example.cocktailcraft.view.click;

import org.example.cocktailcraft.interface_adapter.click.ClickViewModel;
import org.example.cocktailcraft.interface_adapter.click.ClickViewModelListener;
import org.example.cocktailcraft.interface_adapter.click.ClickState;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class ClickView extends JPanel implements ClickViewModelListener {

    private final ClickViewModel clickViewModel;
    private final JLabel errorLabel;
    private final JTextArea ingredientsArea;

    public ClickView(ClickViewModel clickViewModel) {
        this.clickViewModel = clickViewModel;
        this.clickViewModel.addListener(this);

        this.setLayout(new BorderLayout());

        JLabel title = new JLabel("Cocktail Details");
        ingredientsArea = new JTextArea();
        errorLabel = new JLabel();

        ingredientsArea.setEditable(false);

        this.add(title, BorderLayout.NORTH);
        this.add(new JScrollPane(ingredientsArea), BorderLayout.CENTER);
        this.add(errorLabel, BorderLayout.SOUTH);
    }

    @Override
    public void stateChanged() {
        ClickState state = clickViewModel.getState();

        String error = state.getCocktailError();

        if (error != null) {
            errorLabel.setText(error);
            ingredientsArea.setText("");
        } else {
            errorLabel.setText("");
            List<String> ingredients = state.getIngredients();

            ingredientsArea.setText(String.join("\n", ingredients));
        }
    }
}