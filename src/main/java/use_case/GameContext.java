package use_case;

import entity.DomainException;
import entity.Game;
import entity.ResultCode;
import java.util.Objects;
import java.util.Optional;

/** Shared session holder; use cases resolve it on every command after load/start. */
public final class GameContext {
    private Game game;

    public GameContext() { }
    public GameContext(Game game) { replace(game); }

    public Optional<Game> current() { return Optional.ofNullable(game); }

    public Game requireCurrent() {
        if (game == null) {
            throw new DomainException(ResultCode.NO_GAME, "Choose a story or load a game first.");
        }
        return game;
    }

    public void replace(Game validatedCandidate) {
        game = Objects.requireNonNull(validatedCandidate, "validatedCandidate");
    }
}
