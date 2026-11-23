package view;

import app.Main;
import app.ClassChallengeFactory;
import data_access.FileGameStateDataAccessObject;
import entity.GameDay;
import entity.Player;
import interface_adapter.ViewManagerModel;
import interface_adapter.continue_game.ContinueGameController;
import interface_adapter.continue_game.TitleViewModel;
import interface_adapter.rest.RestController;
import interface_adapter.rest.RestViewModel;
import java.awt.Component;
import java.awt.Container;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import support.FakeChallengeState;
import use_case.rest.RestInputBoundary;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.fail;

class GameViewTest {
    @TempDir
    Path directory;

    @Test
    void injectedLecturePageUsesSharedNavigationAndReturnsToStreet() throws Exception {
        FakeChallengeState state = new FakeChallengeState();
        CountDownLatch returned = new CountDownLatch(1);
        AtomicReference<GameView> game = new AtomicReference<>();
        SwingUtilities.invokeAndWait(() -> {
            ViewManagerModel navigation = new ViewManagerModel();
            TitleView title = new TitleView(new TitleViewModel(),
                    new ContinueGameController(() -> fail("Continue is outside this navigation test")),
                    () -> navigation.setActiveView("street"));
            RestInputBoundary unusedRest = new RestInputBoundary() {
                @Override public void execute() { fail("Rest is outside this navigation test"); }
                @Override public void retrySave() { fail("Save is outside this navigation test"); }
            };
            ResidenceView residence = new ResidenceView(new RestViewModel(), new RestController(unusedRest),
                    () -> navigation.setActiveView("street"));
            ClassChallengeView lecture = ClassChallengeFactory.create(state, (topic, purpose, count) -> {
                fail("Entering Lecture Hall must not load questions or start a challenge");
                return List.of();
            }, () -> {
                navigation.setActiveView("street");
                returned.countDown();
            });
            GameView view = new GameView(title, residence, navigation, lecture);
            game.set(view);
            navigation.setActiveView("street");
            button(currentPage(view), "Lecture Hall").doClick();
            assertSame(lecture, currentPage(view));
            // Headless components are not showing, so explicitly refresh on entry.
            lecture.refresh();
        });
        AtomicBoolean ready = new AtomicBoolean();
        long deadline = System.nanoTime() + TimeUnit.SECONDS.toNanos(5);
        while (!ready.get() && System.nanoTime() < deadline) {
            SwingUtilities.invokeAndWait(() -> ready.set(
                    button(currentPage(game.get()), "Back to Street").isEnabled()));
            if (!ready.get()) { Thread.sleep(10); }
        }
        assertTrue(ready.get(), "Lecture Hall did not finish refreshing");
        SwingUtilities.invokeAndWait(() -> button(currentPage(game.get()), "Back to Street").doClick());
        assertTrue(returned.await(5, TimeUnit.SECONDS), "Lecture Hall did not return to Street");
        SwingUtilities.invokeAndWait(() -> assertEquals("street", currentPage(game.get()).getName()));
        assertEquals(100, state.loadOverview().getEnergy());
        assertEquals(0, state.starts);
    }

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
