package use_case.Battle;

import entity.AdventureGame;
import entity.Monster;
import entity.User;

public interface BattleUserDataAccessInterface {
    /**
     * saved the user and the monster
     */
    void save(User user, Monster monster);

    /**
     * return the User that contains the user information which is before user starting the battle
     */
    User getUserBeforeBattle();

    /**
     * return the User that contains the user information which is after user starting th battle
     */
    User getUserAfterBattle();

    /**
     * return the Monster that contains the monster information which is before user starting the battle
     */
    Monster getMonsterBeforeBattle();

    /**
     * return the Monster that contains the monster information which is after user starting the battle
     */
    Monster getMonsterAfterBattle();

    /**
     * Get the current game state
     */
    AdventureGame getGame();

    /**
     * Save the entire game state (including defeated monsters)
     */
    void saveGame(AdventureGame game);

}
