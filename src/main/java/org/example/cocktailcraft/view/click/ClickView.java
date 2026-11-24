package org.example.cocktailcraft.view.click;

import java.awt.BorderLayout;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;

import org.example.cocktailcraft.interface_adapter.click.ClickViewModel;
import org.example.cocktailcraft.interface_adapter.click.ClickViewModelListener;
import org.example.cocktailcraft.interface_adapter.click.ClickState;

public class ClickView extends JPanel implements ClickViewModelListener {
    private final ClickViewModel clickViewModel;
    private final JLabel errorLabel;
    private final JTextArea ingredientsArea;
    private final JTextArea instructionsArea;

    public ClickView(ClickViewModel clickViewModel) {
        this.clickViewModel = clickViewModel;
        this.clickViewModel.addListener(this);
        setLayout(new BorderLayout(10, 10));

        JLabel title = new JLabel("Cocktail Details");
        ingredientsArea = new JTextArea();
        instructionsArea = new JTextArea(6, 30);
        errorLabel = new JLabel();

        ingredientsArea.setEditable(false);
        instructionsArea.setEditable(false);
        instructionsArea.setLineWrap(true);
        instructionsArea.setWrapStyleWord(true);
        instructionsArea.getAccessibleContext().setAccessibleName("Instructions");

        JPanel instructionsPanel = new JPanel(new BorderLayout(5, 5));
        instructionsPanel.add(new JLabel("Instructions:"), BorderLayout.NORTH);
        instructionsPanel.add(new JScrollPane(instructionsArea), BorderLayout.CENTER);
        JSplitPane details = new JSplitPane(
                JSplitPane.VERTICAL_SPLIT,
                new JScrollPane(ingredientsArea),
                instructionsPanel
        );
        details.setResizeWeight(0.6);

        add(title, BorderLayout.NORTH);
        add(details, BorderLayout.CENTER);
        add(errorLabel, BorderLayout.SOUTH);
        stateChanged();
    }

    @Override
    public void stateChanged() {
        ClickState state = clickViewModel.getState();
        String error = state.getCocktailError();
        if (error != null) {
            errorLabel.setText(error);
            ingredientsArea.setText("");
            instructionsArea.setText("");
        } else {
            errorLabel.setText("");
            List<String> ingredients = state.getIngredients();
            ingredientsArea.setText(String.join("\n", ingredients));
            instructionsArea.setText(state.getInstructions());
            instructionsArea.setCaretPosition(0);
        }
    }
}
