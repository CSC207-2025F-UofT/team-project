package entity;

import java.util.LinkedList;
import java.util.List;

public class AdventureGame {

    private final User user;
    private final GameMap gameMap;
    private List<Location> pathHistory;

    // NOTE: could add static data related variables like startTime

    // New Game
    public AdventureGame(User user, GameMap gameMap) {
        this.user = user;
        this.gameMap = gameMap;
        this.pathHistory = new LinkedList<>();
        this.pathHistory.add(gameMap.getCurrentLocation());
    }

    // Old Game
    public AdventureGame(User user, GameMap gameMap,  List<Location> pathHistory) {
        this.user = user;
        this.gameMap = gameMap;
        this.pathHistory = pathHistory;
    }

    public User getUser() {
        return user;
    }

    public GameMap getGameMap() {
        return gameMap;
    }

    public List<Location> getPathHistory() {
        return pathHistory;
    }

    public boolean canMove(Direction direction) {
        return this.gameMap.canMove(direction);
    }

    public boolean move(Direction direction) {
        boolean success = this.gameMap.move(direction);
        if (success) {
            this.pathHistory.add(this.gameMap.getCurrentLocation());
        }
        return success;
    }
}