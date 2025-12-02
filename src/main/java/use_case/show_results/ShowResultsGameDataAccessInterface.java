package use_case.show_results;

import entity.AdventureGame;

/**
 * Data access interface for showing results.
 */
public interface ShowResultsGameDataAccessInterface {
    /**
     * Returns the current game state.
     * @return the adventure game
     */
    AdventureGame get();

    /**
     * Clears the saved game data, forcing a new game on next start.
     */
    void clearGameData();
}
