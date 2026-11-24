package use_case.load_game;

import entity.GameState;
import entity.Player;
import use_case.GamePersistenceException;
import use_case.GameStateDataAccessInterface;
import use_case.SaveGameDataAccessInterface;

import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class LoadGameInteractor implements LoadGameInputBoundary {
    private static final Logger LOGGER = Logger.getLogger(LoadGameInteractor.class.getName());
    private final GameStateDataAccessInterface activeGame;
    private final SaveGameDataAccessInterface saves;
    private final LoadGameOutputBoundary presenter;

    public LoadGameInteractor(GameStateDataAccessInterface activeGame, SaveGameDataAccessInterface saves,
                              LoadGameOutputBoundary presenter) {
        this.activeGame = activeGame;
        this.saves = saves;
        this.presenter = presenter;
    }

    @Override
    public void execute(String username, UUID runId) {
        if (username == null || username.isBlank()) {
            presenter.prepareFailView("Sign in to load your game.");
            return;
        }
        if (runId == null) {
            presenter.prepareFailView("Select a saved game first.");
            return;
        }
        try {
            var savedGame = saves.load(username, runId);
            if (savedGame.isEmpty()) {
                presenter.prepareFailView("That saved game is no longer available.");
                return;
            }
            GameState game = savedGame.get();
            activeGame.saveGameState(game);
            Player player = game.getPlayer();
            presenter.prepareLoadSuccessView(new LoadGameOutputData(game.getDay(), GameState.TOTAL_DAYS,
                    player.getEnergy(), player.getSanity(), player.getAcademicSurvival(), player.getMoney()));
        } catch (GamePersistenceException e) {
            LOGGER.log(Level.WARNING, "Load failed", e);
            presenter.prepareFailView("Couldn't load your game. Please try again.");
        }
    }
}
