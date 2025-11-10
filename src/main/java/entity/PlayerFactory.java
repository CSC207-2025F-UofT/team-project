package entity;

import java.util.List;

/**
 * Factory for creating Player objects.
 */
public class PlayerFactory {

    public Player create() {
        return new Player();
    }

    public Player create(List<ClickableObject> inventory) {
        return new Player(inventory);
    }
}
