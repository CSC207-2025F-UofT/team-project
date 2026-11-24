package use_case.explore;

import entity.Dungeon;
import entity.Inventory;

/**
 * What the Explore use cases need from the data layer: the current dungeon
 * floor and the player's inventory.
 *
 * <p>This interface lives in the use case layer, but it is implemented in the
 * outer data_access layer. That way the interactors depend only on this interface,
 * never on a concrete storage class (in-memory, file, or database). This is the
 * Dependency Inversion Principle.</p>
 */
public interface ExploreDataAccessInterface {

    /**
     * Gets the floor the character is on.
     *
     * @return the current dungeon floor
     */
    Dungeon getDungeon();

    /**
     * Gets the player's inventory.
     *
     * @return the player's inventory
     */
    Inventory getInventory();

    /**
     * Called after the game state changes so the DAO can persist it if it wants to
     * (an in-memory DAO doesn't need to do anything here).
     *
     * @param dungeon   the updated floor
     * @param inventory the updated inventory
     */
    void saveState(Dungeon dungeon, Inventory inventory);
}
