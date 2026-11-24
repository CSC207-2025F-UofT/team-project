package data_access;

import entity.Dungeon;
import entity.Inventory;
import use_case.explore.ExploreDataAccessInterface;

/**
 * Keeps the current dungeon and inventory in memory (lost when the game closes).
 *
 * <p>When Use Case 4 (Save/Load, Darius) is done, his DAO can implement
 * ExploreDataAccessInterface too, and saveState() can write to a file.
 * Neither interactor would need to change.</p>
 */
public class InMemoryExploreDataAccessObject implements ExploreDataAccessInterface {
    /** The current floor. */
    private Dungeon dungeon;
    /** The player's inventory. */
    private Inventory inventory;

    /**
     * Creates the DAO with a starting floor and inventory.
     *
     * @param dungeon   the floor the character starts on
     * @param inventory the player's inventory
     */
    public InMemoryExploreDataAccessObject(Dungeon dungeon, Inventory inventory) {
        this.dungeon = dungeon;
        this.inventory = inventory;
    }

    /** @return the current floor */
    @Override
    public Dungeon getDungeon() {
        return dungeon;
    }

    /** @return the player's inventory */
    @Override
    public Inventory getInventory() {
        return inventory;
    }

    /**
     * Stores the latest state. In memory, this just keeps the references.
     *
     * @param dungeon   the updated floor
     * @param inventory the updated inventory
     */
    @Override
    public void saveState(Dungeon dungeon, Inventory inventory) {
        this.dungeon = dungeon;
        this.inventory = inventory;
    }
}
