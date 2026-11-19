package use_case.new_game;

import entity.GameState;
import entity.Player;
import use_case.GameStateDataAccessInterface;

/**
 * Starts a new game: creates it, stores it as the current game, and hands
 * its starting stats to the presenter.
 */
public class NewGameInteractor implements NewGameInputBoundary {

    private final GameStateDataAccessInterface gameStateDataAccess;
    private final NewGameOutputBoundary presenter;

    public NewGameInteractor(GameStateDataAccessInterface gameStateDataAccess,
                             NewGameOutputBoundary presenter) {
        this.gameStateDataAccess = gameStateDataAccess;
        this.presenter = presenter;
    }

    @Override
    public void execute(NewGameInputData inputData) {
        GameState gameState = GameState.startNew();
        gameStateDataAccess.saveGameState(gameState);

        Player player = gameState.getPlayer();
        NewGameOutputData outputData = new NewGameOutputData(
                gameState.getDay(),
                GameState.TOTAL_DAYS,
                player.getEnergy(),
                player.getSanity(),
                player.getAcademicSurvival(),
                player.getMoney());
        presenter.prepareSuccessView(outputData);
    }
}
