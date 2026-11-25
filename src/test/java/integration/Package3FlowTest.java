package integration;

import entity.DomainResult;
import entity.Game;
import interface_adapter.GameController;
import interface_adapter.GameViewModel;
import interface_adapter.GameViewState;
import java.awt.Component;
import java.awt.Container;
import java.util.Optional;
import java.util.function.BiConsumer;
import javax.swing.JButton;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import org.junit.jupiter.api.Test;
import support.GameFixtures;
import use_case.GameContext;
import use_case.GameOutputAssembler;
import use_case.GameOutputBoundary;
import use_case.InteractInteractor;
import use_case.StartGameInputBoundary;
import use_case.TriviaInputBoundary;
import view.GameView;
import static org.junit.jupiter.api.Assertions.*;

/** Exercises real view -> controller -> interactor -> domain -> output -> view rendering. */
class Package3FlowTest {
    @Test
    void playerCanOpenDrawerReadClueAnswerUnlockAndEscapeThroughRealControls() throws Exception {
        SwingUtilities.invokeAndWait(() -> {
            Game game = GameFixtures.drawerGame();
            GameContext context = new GameContext(game);
            GameOutputAssembler assembler = new GameOutputAssembler();
            GameViewModel model = new GameViewModel(new GameViewState(
                    assembler.fromGame(game, "Explore the room.", Optional.empty()), false));
            GameOutputBoundary presenter = output -> model.setState(new GameViewState(output, model.getState().busy()));
            BiConsumer<DomainResult, Optional<String>> present = (result, activeLock) ->
                    presenter.present(assembler.fromGame(context.requireCurrent(), result.resultText(), activeLock));
            GameController controller = new GameController(new StartGameInputBoundary() {
                public void listStories() { }
                public void start(String id) { }
            }, new InteractInteractor(context, assembler, presenter),
                    id -> present.accept(game.useItems(id), Optional.empty()), new TriviaInputBoundary() {
                        public void open(String id) {
                            presenter.present(assembler.fromGame(game, "Answer the question.", Optional.of(id)));
                        }
                        public void submit(String id, String answer) {
                            present.accept(game.submitAnswer(id, answer), Optional.of(id));
                        }
                    }, () -> present.accept(game.move(), Optional.empty()), () -> { }, () -> { }, Runnable::run);
            GameView view = new GameView(controller, model);

            assertFalse(named(view, "unlock:door", JButton.class).isEnabled());
            named(view, "action:drawer:open", JButton.class).doClick(0);
            assertEquals(1, game.inventoryItems().size());
            assertTrue(named(view, "narrative", JTextArea.class).getText().contains("Obtained: Laboratory access card"));
            assertTrue(named(view, "unlock:door", JButton.class).isEnabled());
            assertFalse(named(view, "move", JButton.class).isEnabled());

            named(view, "action:drawer:read", JButton.class).doClick(0);
            assertTrue(named(view, "narrative", JTextArea.class).getText().contains("the answer is 007"));
            assertFalse(named(view, "narrative", JTextArea.class).getText().contains("Obtained:"));

            named(view, "trivia:question", JButton.class).doClick(0);
            named(view, "answer", JTextField.class).setText("wrong");
            named(view, "submitAnswer", JButton.class).doClick(0);
            assertTrue(model.getState().data().activeTrivia().isPresent());
            assertTrue(named(view, "narrative", JTextArea.class).getText().contains("incorrect"));
            named(view, "answer", JTextField.class).setText("007");
            named(view, "submitAnswer", JButton.class).doClick(0);
            assertFalse(named(view, "move", JButton.class).isEnabled());
            named(view, "unlock:door", JButton.class).doClick(0);
            assertTrue(named(view, "move", JButton.class).isEnabled());
            assertEquals("Escape", named(view, "move", JButton.class).getText());
            named(view, "move", JButton.class).doClick(0);
            assertTrue(model.getState().data().escaped());
            assertFalse(named(view, "move", JButton.class).isEnabled());
            assertTrue(named(view, "save", JButton.class).isEnabled());
        });
    }

    private static <T extends Component> T named(Container parent, String name, Class<T> type) {
        for (Component child : parent.getComponents()) {
            if (name.equals(child.getName()) && type.isInstance(child)) return type.cast(child);
            if (child instanceof Container) {
                T found = find((Container) child, name, type);
                if (found != null) return found;
            }
        }
        throw new AssertionError("Missing control " + name);
    }

    private static <T extends Component> T find(Container parent, String name, Class<T> type) {
        for (Component child : parent.getComponents()) {
            if (name.equals(child.getName()) && type.isInstance(child)) return type.cast(child);
            if (child instanceof Container) {
                T found = find((Container) child, name, type);
                if (found != null) return found;
            }
        }
        return null;
    }
}
