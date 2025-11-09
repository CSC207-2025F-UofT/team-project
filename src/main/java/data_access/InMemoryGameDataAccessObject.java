package data_access;

import entity.Player;
import entity.Scene;
import use_case.switch_to_game.SwitchToGameViewDataAccessInterface;

/**
 * In-memory implementation of game data access.
 */
public class InMemoryGameDataAccessObject implements SwitchToGameViewDataAccessInterface {

    private Scene currentScene;
    private Player player;

    public InMemoryGameDataAccessObject(Scene initialScene, Player player) {
        this.currentScene = initialScene;
        this.player = player;
    }

    @Override
    public Scene getCurrentScene() {
        return currentScene;
    }

    public void setCurrentScene(Scene scene) {
        this.currentScene = scene;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }
}
