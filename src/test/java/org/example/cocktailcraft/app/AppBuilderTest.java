package org.example.cocktailcraft.app;

import org.example.cocktailcraft.view.browse.MainPageView;
import org.junit.jupiter.api.Test;

import java.awt.Component;
import java.awt.Container;
import java.awt.GraphicsEnvironment;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assumptions.assumeFalse;

class AppBuilderTest {
    @Test
    void opensHomeWithSearchConnected() throws Exception {
        assumeFalse(GraphicsEnvironment.isHeadless(), "Requires a graphical environment");
        SwingUtilities.invokeAndWait(() -> {
            JFrame frame = new AppBuilder()
                    .addSearchView()
                    .addSearchUseCase()
                    .addClickUseCase()
                    .addMainPageView()
                    .build();
            try {
                assertEquals("CocktailCraft", frame.getTitle());
                assertTrue(frame.getContentPane() instanceof MainPageView);
                assertTrue(findButton(frame.getContentPane(), "Base spirits").isEnabled());
                for (String label : new String[] {"Search", "Clear"}) {
                    JButton button = findButton(frame.getContentPane(), label);
                    assertNotNull(button, label);
                    assertTrue(button.isEnabled(), label);
                    assertEquals(1, button.getActionListeners().length, label);
                }
                assertFalse(findButton(frame.getContentPane(), "Profile").isEnabled());
            } finally {
                frame.dispose();
            }
        });
    }

    private static JButton findButton(Container parent, String label) {
        for (Component child : parent.getComponents()) {
            if (child instanceof JButton && ((JButton) child).getText().equals(label)) {
                return (JButton) child;
            }
            if (child instanceof Container) {
                JButton found = findButton((Container) child, label);
                if (found != null) {
                    return found;
                }
            }
        }
        return null;
    }
}
