package org.example.cocktailcraft.app;

import org.junit.jupiter.api.Test;

import java.awt.Component;
import java.awt.Container;
import java.awt.GraphicsEnvironment;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assumptions.assumeFalse;

class AppBuilderTest {
    @Test
    void assemblesAllPreviewPagesAndWiresFeatureStatus() throws Exception {
        assumeFalse(GraphicsEnvironment.isHeadless(), "Requires a graphical environment");
        SwingUtilities.invokeAndWait(() -> {
            JFrame frame = new AppBuilder().build();
            try {
                assertEquals("CocktailCraft", frame.getTitle());
                assertEquals(6, frame.getContentPane().getComponentCount());
                findButton(frame, "Search").doClick();
                assertTrue(hasLabel(frame, "Not implemented: Search (Qinli)."));
                findButton(frame, "Profile").doClick();
                assertTrue(SwingUtilities.getAncestorOfClass(
                        org.example.cocktailcraft.view.ProfileView.class,
                        findButton(frame, "Login")).isVisible());
                findButton(frame, "Login").doClick();
                findButton(frame, "Login (scaffold)").doClick();
                assertTrue(hasLabel(frame, "Not implemented: Login (Jiaqi)."));
                findButton(frame, "Back to Profile").doClick();
                findButton(frame, "Home").doClick();
                findButton(frame, "Preview Details layout").doClick();
                findButton(frame, "Favourite (scaffold)").doClick();
                assertTrue(hasLabel(frame, "Not implemented: Favourite (Wenqian)."));
                findButton(frame, "Preview Customize layout").doClick();
                assertNotNull(findButton(frame, "Save (not implemented)"));
                assertTrue(!findButton(frame, "Save (not implemented)").isEnabled());
            } finally {
                frame.dispose();
            }
        });
    }

    private static JButton findButton(Container parent, String text) {
        for (Component child : parent.getComponents()) {
            if (child instanceof JButton && ((JButton) child).getText().equals(text)) {
                return (JButton) child;
            }
            if (child instanceof Container) {
                JButton found = findButton((Container) child, text);
                if (found != null) {
                    return found;
                }
            }
        }
        return null;
    }

    private static boolean hasLabel(Container parent, String text) {
        for (Component child : parent.getComponents()) {
            if (child instanceof JLabel && ((JLabel) child).getText().equals(text)) {
                return true;
            }
            if (child instanceof Container && hasLabel((Container) child, text)) {
                return true;
            }
        }
        return false;
    }
}
