package view;

import app.Main;
import data_access.FileGameStateDataAccessObject;
import entity.GameDay;
import entity.Player;
import java.awt.Component;
import java.awt.Container;
import java.nio.file.Path;
import java.time.LocalDate;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class GameViewTest {
    @TempDir
    Path directory;

    @Test
    void navigateToBuildingsAndBack() throws Exception {
        SwingUtilities.invokeAndWait(() -> {
            JPanel root = Main.createView(directory.resolve("checkpoint.json"));
            GameView view = (GameView) root.getComponent(0);
            assertEquals("title", currentPage(view).getName());
            button(currentPage(view), "Continue").doClick();
            assertEquals("title", currentPage(view).getName());
            assertTrue(hasText(currentPage(view), "No saved game is available."));
            button(currentPage(view), "New Game").doClick();
            view = (GameView) root.getComponent(0);

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

    @Test
    void continueRestoresResidenceAndNewGameStartsFresh() throws Exception {
        Path checkpoint = directory.resolve("checkpoint.json");
        new FileGameStateDataAccessObject(checkpoint,
                new GameDay(4, LocalDate.of(2025, 9, 2)), new Player(35, 100)).saveCheckpoint();
        SwingUtilities.invokeAndWait(() -> {
            JPanel root = Main.createView(checkpoint);
            GameView view = (GameView) root.getComponent(0);
            button(currentPage(view), "Continue").doClick();
            assertEquals("residence", currentPage(view).getName());
            assertTrue(hasText(currentPage(view), "Day 4"));
            assertTrue(hasText(currentPage(view), "Energy: 35 / 100"));
            button(currentPage(view), "Back to Street").doClick();
            button(currentPage(view), "Title Screen").doClick();
            button(currentPage(view), "New Game").doClick();
            view = (GameView) root.getComponent(0);
            button(currentPage(view), "Innis Residence").doClick();
            assertTrue(hasText(currentPage(view), "Day 1"));
            assertTrue(hasText(currentPage(view), "Energy: 100 / 100"));
        });
        FileGameStateDataAccessObject saved = new FileGameStateDataAccessObject(checkpoint,
                new GameDay(1, LocalDate.of(2025, 9, 2)), new Player(100, 100));
        saved.loadCheckpoint();
        assertEquals(4, saved.getCurrentDay().getDayNumber());
    }

    private boolean hasText(Container page, String text) {
        for (Component component : page.getComponents()) {
            if (component instanceof JLabel && ((JLabel) component).getText() != null
                    && ((JLabel) component).getText().contains(text)) {
                return true;
            }
        }
        return false;
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
