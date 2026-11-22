package data_access;

import entity.GameState;
import use_case.GameStateDataAccessInterface;

/**
 * Keeps the current game in memory while the app is running. Story 3
 * (save/load) adds SQLite persistence behind the save/load interfaces;
 * this class stays as the live "current game" holder.
 */
public class InMemoryGameStateDataAccessObject implements GameStateDataAccessInterface {

    private GameState gameState;

    @Override
    public void saveGameState(GameState gameState) {
        this.gameState = gameState;
    }

    @Override
    public GameState getGameState() {
        return gameState;
    }
}
