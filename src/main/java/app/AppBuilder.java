package app;

import data_access.JsonStoryTemplateDataAccessObject;
import interface_adapter.*;
import java.util.Optional;
import java.util.Random;
import javax.swing.SwingUtilities;
import use_case.*;
import view.GameView;

/** Composition root for the packages available on main. Construct on the EDT. */
public final class AppBuilder implements AutoCloseable {
    private final GameContext context = new GameContext();
    private final GameOutputAssembler assembler = new GameOutputAssembler();
    private final GameViewModel model = new GameViewModel();
    private final GameOutputBoundary output = data -> SwingUtilities.invokeLater(() ->
            model.setState(new GameViewState(data, model.getState().busy())));
    private final SwingCommandDispatcher commands = new SwingCommandDispatcher(model,
            error -> present(error.getMessage(), Optional.empty()));
    private final GameController controller;
    private final GameView view;

    public AppBuilder(StoryTemplateDataAccessInterface templates) {
        StartGameInputBoundary start = new StartGameInteractor(context, new GameFactory(new Random()),
                templates, assembler, output);
        TriviaInputBoundary trivia = new TriviaInputBoundary() {
            public void open(String lockId) {
                boolean retained = context.requireCurrent().triviaFor(lockId).isPresent();
                present(retained ? "Enter your answer." : "The online trivia provider is unavailable.",
                        Optional.of(lockId));
            }

            public void submit(String lockId, String answer) {
                present(context.requireCurrent().submitAnswer(lockId, answer).resultText(), Optional.of(lockId));
            }
        };
        // Replace these bindings as the feature owners merge their use cases.
        controller = new GameController(start, new InteractInteractor(context, assembler, output),
                lockId -> present(context.requireCurrent().useItems(lockId).resultText(), Optional.empty()),
                trivia, () -> present(context.requireCurrent().move().resultText(), Optional.empty()),
                () -> present("Saving is unavailable in this build.", Optional.empty()),
                () -> present("Loading is unavailable in this build.", Optional.empty()), commands);
        view = new GameView(controller, model);
    }

    public AppBuilder() { this(new JsonStoryTemplateDataAccessObject()); }

    private void present(String message, Optional<String> lockId) {
        output.present(context.current().map(game -> assembler.fromGame(game, message, lockId))
                .orElseGet(() -> assembler.fromNoGame(message)));
    }

    public GameView view() { return view; }
    public void listStories() { controller.listStories(); }
    public void close() { commands.close(); }
}
