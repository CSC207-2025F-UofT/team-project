import data_access.InMemoryGameStateDataAccessObject;
import data_access.SchemaInitializer;
import data_access.SqliteConnectionProvider;
import data_access.SqliteGameDataAccessObject;
import entity.GameState;
import entity.Player;
import interface_adapter.ViewManagerModel;
import interface_adapter.hud.HudViewModel;
import interface_adapter.new_game.NewGameController;
import interface_adapter.new_game.NewGamePresenter;
import interface_adapter.persistence.PersistenceController;
import interface_adapter.persistence.PersistencePresenter;
import interface_adapter.persistence.PersistenceViewModel;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import use_case.ManageSavesInteractor;
import use_case.SavedGameSummary;
import use_case.load_game.LoadGameInteractor;
import use_case.new_game.NewGameInteractor;
import use_case.save_game.SaveGameInteractor;
import view.GameView;
import view.StartView;

import javax.swing.JButton;
import javax.swing.SwingUtilities;
import java.awt.Component;
import java.awt.Container;
import java.nio.file.Path;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

class PersistenceIntegrationTest {
    @TempDir
    Path directory;

    @Test
    void buttonsReportMissingSaveAndRestoreLiveStateAndHud() throws Exception {
        var connections = new SqliteConnectionProvider(directory.resolve("game.db"));
        new SchemaInitializer(connections).initialize();
        try (var connection = connections.openConnection(); var statement = connection.createStatement()) {
            statement.executeUpdate("INSERT INTO users (username, password_hash) VALUES ('alice', 'hash')");
        }

        var active = new InMemoryGameStateDataAccessObject();
        var saves = new SqliteGameDataAccessObject(connections);
        var feedback = new PersistenceViewModel();
        var hud = new HudViewModel();
        var views = new ViewManagerModel();
        var presenter = new PersistencePresenter(feedback, hud, views, GameView.VIEW_NAME);
        var controller = new PersistenceController(new SaveGameInteractor(active, saves, presenter),
                new LoadGameInteractor(active, saves, presenter),
                new ManageSavesInteractor(saves, presenter), () -> "alice", feedback);
        var messages = new LinkedBlockingQueue<String>();
        var selections = new LinkedBlockingQueue<List<SavedGameSummary>>();
        var nameRequests = new LinkedBlockingQueue<Boolean>();
        feedback.addPropertyChangeListener(event -> {
            if (PersistenceViewModel.SAVES_UPDATED.equals(event.getPropertyName())) {
                selections.add(feedback.getSaves());
            } else if (PersistenceViewModel.REQUEST_NAME.equals(event.getPropertyName())) {
                nameRequests.add(true);
            } else if ("state".equals(event.getPropertyName()) && !feedback.getState().busy()
                    && !feedback.getState().message().isEmpty()) {
                messages.add(feedback.getState().message());
            }
        });
        var start = new StartView(new NewGameController(new NewGameInteractor(active,
                new NewGamePresenter(hud, views, GameView.VIEW_NAME))));
        var game = new GameView(hud);

        SwingUtilities.invokeAndWait(() -> {
            start.addLoadGame(controller, feedback);
            game.addSaveGame(controller, feedback);
            button(start, "Load Game").doClick();
        });
        assertEquals(List.of(), selections.poll(10, TimeUnit.SECONDS));
        assertNull(active.getGameState());

        var original = new GameState(3, new Player(72, 65, 83, 34), "residence");
        SwingUtilities.invokeAndWait(() -> {
            active.saveGameState(original);
            button(game, "Save Game").doClick();
        });
        assertEquals(true, nameRequests.poll(10, TimeUnit.SECONDS));
        SwingUtilities.invokeAndWait(() -> controller.save("a".repeat(41)));
        assertEquals("Use up to 40 characters with no line breaks or control characters.",
                messages.poll(10, TimeUnit.SECONDS));
        assertTrue(saves.list("alice").isEmpty());
        SwingUtilities.invokeAndWait(() -> controller.save("First Run"));
        assertEquals("Game saved.", messages.poll(10, TimeUnit.SECONDS));

        SwingUtilities.invokeAndWait(() -> {
            button(start, "New Game").doClick();
            assertEquals(1, hud.getState().getDay());
            button(game, "Load Game").doClick();
        });
        var listed = selections.poll(10, TimeUnit.SECONDS);
        assertNotNull(listed);
        assertEquals(1, listed.size());
        assertEquals("First Run", listed.get(0).name());
        SwingUtilities.invokeAndWait(() -> controller.load(listed.get(0).runId()));
        assertEquals("Game loaded.", messages.poll(10, TimeUnit.SECONDS));
        SwingUtilities.invokeAndWait(() -> {
            assertEquals(original.getRunId(), active.getGameState().getRunId());
            assertEquals(GameView.VIEW_NAME, views.getState());
            assertEquals(3, hud.getState().getDay());
            assertEquals(72, hud.getState().getEnergy());
            assertEquals(65, hud.getState().getSanity());
            assertEquals(83, hud.getState().getAcademicSurvival());
            assertEquals(34, hud.getState().getMoney());
        });

        SwingUtilities.invokeAndWait(() -> controller.delete(original.getRunId()));
        assertEquals("Saved game deleted.", messages.poll(10, TimeUnit.SECONDS));
        assertEquals(List.of(), selections.poll(10, TimeUnit.SECONDS));
        SwingUtilities.invokeAndWait(() -> controller.load(original.getRunId()));
        assertEquals("That saved game is no longer available.", messages.poll(10, TimeUnit.SECONDS));
        SwingUtilities.invokeAndWait(() -> assertEquals(original.getRunId(), active.getGameState().getRunId()));
    }

    private static JButton button(Container container, String text) {
        for (Component component : container.getComponents()) {
            if (component instanceof JButton button && text.equals(button.getText())) {
                return button;
            }
            if (component instanceof Container child) {
                JButton found = button(child, text);
                if (found != null) {
                    return found;
                }
            }
        }
        return null;
    }
}
