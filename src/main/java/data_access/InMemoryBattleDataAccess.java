package data_access;

import entity.Monster;
import entity.User;
import use_case.Battle.BattleUserDataAccessInterface;

/**
 * In-Memory implementation of BattleUserDataAccessInterface.
 * Stores battle state in memory during the battle.
 */
public class InMemoryBattleDataAccess implements BattleUserDataAccessInterface {

    // Store battle states (before and after)
    private User userBeforeBattle;
    private User userAfterBattle;
    private Monster monsterBeforeBattle;
    private Monster monsterAfterBattle;

    /**
     * Saves the current state of user and monster.
     * This creates a snapshot of the current battle state.
     */
    @Override
    public void save(User user, Monster monster) {
        // If this is the first save (before battle starts)
        if (userBeforeBattle == null) {
            userBeforeBattle = cloneUser(user);
            monsterBeforeBattle = cloneMonster(monster);
        }

        // Always update the "after" state
        userAfterBattle = cloneUser(user);
        monsterAfterBattle = cloneMonster(monster);
    }

    /**
     * Returns the user state before the battle started.
     */
    @Override
    public User getUserBeforeBattle() {
        return userBeforeBattle;
    }

    /**
     * Returns the user state after the battle.
     */
    @Override
    public User getUserAfterBattle() {
        return userAfterBattle;
    }

    /**
     * Returns the monster state before the battle started.
     */
    @Override
    public Monster getMonsterBeforeBattle() {
        return monsterBeforeBattle;
    }

    /**
     * Returns the monster state after the battle.
     */
    @Override
    public Monster getMonsterAfterBattle() {
        return monsterAfterBattle;
    }

    /**
     * Resets the battle state (for starting a new battle).
     */
    public void reset() {
        userBeforeBattle = null;
        userAfterBattle = null;
        monsterBeforeBattle = null;
        monsterAfterBattle = null;
    }

    /**
     * Creates a shallow clone of the User.
     * For a real implementation, we might need deep cloning.
     */
    private User cloneUser(User user) {
        if (user == null) return null;

        // For now, just return the reference
        // TODO: Implement proper cloning if we need to preserve history
        return user;
    }

    /**
     * Creates a shallow clone of the Monster.
     * For a real implementation, we might need deep cloning.
     */
    private Monster cloneMonster(Monster monster) {
        if (monster == null) return null;

        // For now, just return the reference
        // TODO: Implement proper cloning if we need to preserve history
        return monster;
    }
}
