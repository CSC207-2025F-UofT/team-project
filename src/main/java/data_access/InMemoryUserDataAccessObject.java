package data_access;

import entity.User;
import entity.AdventureGame;
import entity.GameMap;
import entity.Location;
import use_case.move.MoveGameDataAccessInterface;

import java.util.Arrays;
import java.util.List;

/**
 * In-memory implementation of MoveGameDataAccessInterface.
 * Data is stored in memory only and will be lost when the application closes.
 * Useful for testing and development.
 */
public class InMemoryUserDataAccessObject implements MoveGameDataAccessInterface {

    private AdventureGame game;

    public InMemoryUserDataAccessObject() {
        startNewGame();
    }

    private void startNewGame() {
        User user = new User();
        Location loc0 = new Location("Forest", 43.6532, -79.3832, null);
        Location loc1 = new Location("Cave", 43.6540, -79.3840, null);
        Location loc2 = new Location("Mountain", 43.6550, -79.3850, null);

        List<Location> locations = Arrays.asList(loc0, loc1, loc2);

        GameMap gameMap = new GameMap(locations, 0);
        this.game = new AdventureGame(user, gameMap);
    }

    @Override
    public AdventureGame getGame() {
        return this.game;
    }

    @Override
    public void saveGame(AdventureGame game) {
        // Only update in-memory state, no persistence
        this.game = game;
    }
}
