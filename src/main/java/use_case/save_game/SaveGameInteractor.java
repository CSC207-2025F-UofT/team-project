package use_case.save_game;

import entity.GameState;
import use_case.GamePersistenceException;
import use_case.GameStateDataAccessInterface;
import use_case.SaveGameDataAccessInterface;

import java.util.logging.Level;
import java.util.logging.Logger;

public final class SaveGameInteractor implements SaveGameInputBoundary {
    private static final Logger LOGGER = Logger.getLogger(SaveGameInteractor.class.getName());
    private final GameStateDataAccessInterface activeGame;
    private final SaveGameDataAccessInterface saves;
    private final SaveGameOutputBoundary presenter;

    public SaveGameInteractor(GameStateDataAccessInterface activeGame, SaveGameDataAccessInterface saves,
                              SaveGameOutputBoundary presenter) {
        this.activeGame = activeGame;
        this.saves = saves;
        this.presenter = presenter;
    }

    @Override
    public void execute(String username) {
        execute(username, null);
    }

    @Override
    public void execute(String username, String name) {
        if (username == null || username.isBlank()) {
            presenter.prepareFailView("Sign in to save your game.");
            return;
        }
        GameState game = activeGame.getGameState();
        if (game == null) {
            presenter.prepareFailView("Start a game before saving.");
            return;
        }
        try {
            if (name == null && saves.load(username, game.getRunId()).isEmpty()) {
                presenter.prepareNameView();
                return;
            }
            String cleanName = name == null ? "" : name.strip();
            if (cleanName.length() > 40 || cleanName.chars().anyMatch(Character::isISOControl)) {
                presenter.prepareFailView("Use up to 40 characters with no line breaks or control characters.");
                return;
            }
            saves.save(username, game, cleanName);
            presenter.prepareSaveSuccessView();
        } catch (GamePersistenceException e) {
            LOGGER.log(Level.WARNING, "Save failed", e);
            presenter.prepareFailView("Couldn't save your game. Please try again.");
        }
    }
}
