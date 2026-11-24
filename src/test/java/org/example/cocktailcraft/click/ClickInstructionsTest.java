package org.example.cocktailcraft.click;

import java.awt.Component;
import java.awt.Container;
import java.util.List;
import java.util.Set;

import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

import org.example.cocktailcraft.entity.Cocktail;
import org.example.cocktailcraft.entity.RecipeIngredient;
import org.example.cocktailcraft.entity.Tags;
import org.example.cocktailcraft.interface_adapter.click.ClickController;
import org.example.cocktailcraft.interface_adapter.click.ClickPresenter;
import org.example.cocktailcraft.interface_adapter.click.ClickViewModel;
import org.example.cocktailcraft.use_case.click.ClickDataAccessInterface;
import org.example.cocktailcraft.use_case.click.ClickInteractor;
import org.example.cocktailcraft.view.click.ClickView;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

class ClickInstructionsTest {
    @Test
    void displaysOriginalStepsAndClearsThemWhenDetailsFail() throws Exception {
        Cocktail cocktail = new Cocktail("mojito", "Mojito",
                List.of(new RecipeIngredient("Sugar", "1", "tsp")), new Tags(Set.of()),
                "1. Add sugar.\n2. Stir with ice.");
        ClickDataAccessInterface dao = new ClickDataAccessInterface() {
            @Override
            public Cocktail get(String id) {
                return cocktail.getId().equals(id) ? cocktail : null;
            }

            @Override
            public boolean existsByID(String id) {
                return get(id) != null;
            }
        };
        SwingUtilities.invokeAndWait(() -> {
            ClickViewModel model = new ClickViewModel();
            ClickView view = new ClickView(model);
            ClickController controller = new ClickController(new ClickInteractor(dao, new ClickPresenter(model)));
            controller.execute("mojito");
            JTextArea instructions = findInstructions(view);
            assertEquals(cocktail.getInstructions(), instructions.getText());
            assertEquals(cocktail.getInstructions(), model.getState().getInstructions());
            assertFalse(instructions.isEditable());
            controller.execute("missing");
            assertEquals("", instructions.getText());
            controller.execute("mojito");
            assertEquals(cocktail.getInstructions(), instructions.getText());
        });
    }

    private JTextArea findInstructions(Container parent) {
        for (Component component : parent.getComponents()) {
            if (component instanceof JTextArea
                    && "Instructions".equals(((JTextArea) component).getAccessibleContext().getAccessibleName())) {
                return (JTextArea) component;
            }
            if (component instanceof Container) {
                JTextArea result = findInstructions((Container) component);
                if (result != null) {
                    return result;
                }
            }
        }
        return null;
    }
}
