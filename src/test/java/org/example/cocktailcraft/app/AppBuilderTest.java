package org.example.cocktailcraft.app;

import java.awt.Component;
import java.awt.Container;
import java.awt.GraphicsEnvironment;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

import org.example.cocktailcraft.data_access.CocktailJsonReader;
import org.example.cocktailcraft.entity.Cocktail;
import org.example.cocktailcraft.view.browse.MainPageView;
import org.example.cocktailcraft.view.customize.CustomizeView;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assumptions.assumeFalse;

class AppBuilderTest {
    @Test
    void opensSelectedRecipeInstructionsAndCancelReturnsHome() throws Exception {
        assumeFalse(GraphicsEnvironment.isHeadless(), "Requires a graphical environment");
        Cocktail expected = new CocktailJsonReader().readAll().get(0);
        SwingUtilities.invokeAndWait(() -> {
            JFrame frame = new AppBuilder()
                    .addSearchView()
                    .addSearchUseCase()
                    .addClickUseCase()
                    .addCustomizeUseCase(() -> null)
                    .addMainPageView()
                    .build();
            try {
                assertEquals("CocktailCraft", frame.getTitle());
                MainPageView home = find(frame.getContentPane(), MainPageView.class, null);
                CustomizeView customize = find(frame.getContentPane(), CustomizeView.class, null);
                assertTrue(find(home, JButton.class, "Base spirits").isEnabled());
                for (String label : new String[] {"Search", "Clear"}) {
                    JButton searchButton = find(home, JButton.class, label);
                    assertTrue(searchButton.isEnabled(), label);
                    assertEquals(1, searchButton.getActionListeners().length, label);
                }
                assertFalse(find(home, JButton.class, "Profile").isEnabled());
                assertTrue(home.isVisible());
                assertFalse(customize.isVisible());
                JButton button = find(home, JButton.class, "Customize");
                assertFalse(button.isEnabled());
                find(home, JList.class, null).setSelectedIndex(0);
                assertTrue(button.isEnabled());
                button.doClick(0);
                assertTrue(customize.isVisible());
                assertFalse(home.isVisible());
                JTextArea instructions = find(customize, JTextArea.class, null);
                assertEquals(expected.getInstructions(), instructions.getText());
                instructions.setText("Unsaved replacement steps.");
                find(customize, JButton.class, "Cancel").doClick(0);
                assertTrue(home.isVisible());
                assertFalse(customize.isVisible());
                button.doClick(0);
                assertEquals(expected.getInstructions(), instructions.getText());
            } finally {
                frame.dispose();
            }
        });
    }

    private static <T extends Component> T find(Container parent, Class<T> type, String text) {
        T result = findOrNull(parent, type, text);
        assertNotNull(result, "Missing " + type.getSimpleName() + " " + text);
        return result;
    }

    private static <T extends Component> T findOrNull(Container parent, Class<T> type, String text) {
        for (Component child : parent.getComponents()) {
            if (type.isInstance(child)
                    && (text == null || child instanceof JButton && text.equals(((JButton) child).getText()))) {
                return type.cast(child);
            }
            if (child instanceof Container) {
                T result = findOrNull((Container) child, type, text);
                if (result != null) {
                    return result;
                }
            }
        }
        return null;
    }
}
