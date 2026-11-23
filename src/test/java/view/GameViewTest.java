package view;

import java.awt.Component;
import java.awt.Container;
import javax.swing.JButton;
import javax.swing.SwingUtilities;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

class GameViewTest {
    @Test
    void navigateToBuildingsAndBack() throws Exception {
        SwingUtilities.invokeAndWait(() -> {
            GameView view = new GameView();
            assertEquals("title", currentPage(view).getName());
            assertFalse(button(currentPage(view), "Continue").isEnabled());
            button(currentPage(view), "New Game").doClick();

            String[][] buildings = {
                {"Registrar's Office", "registrar"},
                {"Lecture Hall", "lecture"},
                {"Exam Centre", "exam"},
                {"Innis Residence", "residence"}
            };
            for (String[] building : buildings) {
                assertEquals("street", currentPage(view).getName());
                button(currentPage(view), building[0]).doClick();
                assertEquals(building[1], currentPage(view).getName());
                button(currentPage(view), "Back to Street").doClick();
            }
            button(currentPage(view), "Title Screen").doClick();
            assertEquals("title", currentPage(view).getName());
        });
    }

    private Container currentPage(GameView view) {
        for (Component page : view.getComponents()) {
            if (page.isVisible()) {
                return (Container) page;
            }
        }
        throw new AssertionError("No page is visible");
    }

    private JButton button(Container page, String text) {
        for (Component component : page.getComponents()) {
            if (component instanceof JButton && text.equals(((JButton) component).getText())) {
                return (JButton) component;
            }
        }
        throw new AssertionError("Button not found: " + text);
    }
}
