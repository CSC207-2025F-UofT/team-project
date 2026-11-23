package entity;

/**
 * Factory for creating Player objects.
 */
public class PlayerFactory {

    public Player create(String username) {
        return new Player(username);
    }
}
