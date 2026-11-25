package use_case;

import entity.DomainException;
import entity.DomainResult;
import entity.Game;
import java.util.Objects;
import java.util.Optional;

/** Executes one atomic interaction and presents fresh state for every outcome. */
public final class InteractInteractor implements InteractInputBoundary {
    private final GameContext context;
    private final GameOutputAssembler assembler;
    private final GameOutputBoundary output;

    public InteractInteractor(GameContext context, GameOutputAssembler assembler,
                              GameOutputBoundary output) {
        this.context = Objects.requireNonNull(context, "context");
        this.assembler = Objects.requireNonNull(assembler, "assembler");
        this.output = Objects.requireNonNull(output, "output");
    }

    @Override
    public void execute(String objectId, String actionId, Optional<String> itemId) {
        String message;
        try {
            Game game = context.requireCurrent();
            DomainResult result = game.interact(objectId, actionId, itemId);
            message = result.resultText();
        } catch (DomainException exception) {
            message = exception.getMessage();
        }
        // Do not retain a Game: start/load may have replaced the context.
        Optional<Game> current = context.current();
        output.present(current.isPresent()
                ? assembler.fromGame(current.get(), message, Optional.empty())
                : assembler.fromNoGame(message));
    }
}
