package use_case;

import entity.Collectable;
import entity.Game;
import java.util.Optional;
import java.util.stream.Collectors;

public final class StartGameInteractor implements StartGameInputBoundary {
    private final GameContext context;
    private final GameFactory factory;
    private final StoryTemplateDataAccessInterface templates;
    private final GameOutputAssembler assembler;
    private final GameOutputBoundary output;

    public StartGameInteractor(GameContext context, GameFactory factory, StoryTemplateDataAccessInterface templates,
                               GameOutputAssembler assembler, GameOutputBoundary output) {
        this.context = context;
        this.factory = factory;
        this.templates = templates;
        this.assembler = assembler;
        this.output = output;
    }

    public void listStories() {
        GameOutputData data;
        try {
            data = assembler.fromStories(templates.listTemplates(), "Select a story to begin.");
        } catch (RuntimeException e) {
            data = failure(e);
        }
        output.present(data);
    }

    public void start(String templateId) {
        GameOutputData data;
        try {
            Game game = factory.create(templates.load(templateId));
            String names = game.inventoryItems().stream().map(Collectable::name).collect(Collectors.joining(", "));
            data = assembler.fromGame(game, names.isEmpty() ? "" : "Obtained: " + names + ".", Optional.empty());
            context.replace(game);
        } catch (RuntimeException e) {
            data = failure(e);
        }
        output.present(data);
    }

    private GameOutputData failure(RuntimeException e) {
        String message = "Story setup failed: " + e.getMessage();
        return context.current().map(game -> assembler.fromGame(game, message, Optional.empty()))
                .orElseGet(() -> assembler.fromNoGame(message));
    }
}
