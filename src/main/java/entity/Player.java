package entity;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents the player in the game with an inventory.
 */
public class Player {

    private final List<ClickableObject> inventory;

    /**
     * Creates a new player with an empty inventory.
     */
    public Player() {
        this.inventory = new ArrayList<>();
    }

    /**
     * Creates a new player with the given inventory.
     * @param inventory the initial inventory
     */
    public Player(List<ClickableObject> inventory) {
        this.inventory = new ArrayList<>(inventory);
    }

    public List<ClickableObject> getInventory() {
        return new ArrayList<>(inventory);
    }

    /**
     * Adds an object to the player's inventory.
     * @param object the object to add
     */
    public void addToInventory(ClickableObject object) {
        inventory.add(object);
    }

    /**
     * Removes an object from the player's inventory.
     * @param object the object to remove
     */
    public void removeFromInventory(ClickableObject object) {
        inventory.remove(object);
    }
}
