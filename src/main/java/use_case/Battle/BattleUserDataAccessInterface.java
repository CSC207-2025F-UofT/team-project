package use_case.Battle;

import entity.AdventureGame;
import entity.Monster;
import entity.User;

public interface BattleUserDataAccessInterface {
    /**
     * Get the current game state
     */
    AdventureGame getGame();

    /**
     * Save the entire game state (including defeated monsters)
     */
    void saveGame(AdventureGame game);

    /**
     * Load the entire game state
     */
    void loadGameData();
}
