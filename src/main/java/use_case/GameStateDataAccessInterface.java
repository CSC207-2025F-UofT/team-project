package use_case;

import entity.GameState;

/**
 * Where the current game lives while the app is running. Shared by every
 * use case that reads or changes the game (new game, navigate, talk, ...),
 * so it sits in the use_case package rather than in one use case's folder.
 *
 * The use case layer owns this interface and data_access implements it
 * (Dependency Inversion). Saved snapshots use SaveGameDataAccessInterface.
 */
public interface GameStateDataAccessInterface {

    /** Stores the given game as the current game, replacing any previous one. */
    void saveGameState(GameState gameState);

    /** Returns the current game, or null if no game has been started. */
    GameState getGameState();
}
