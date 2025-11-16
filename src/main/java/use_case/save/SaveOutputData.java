package use_case.save;

import entity.Scene;
import entity.Player;

import java.util.List;

/**
 * Output data for the save use case.
 */
public class SaveOutputData {

    private Scene currentScene;
    private List<Scene> scenes;
    private Player player;

    public Scene getCurrentScene() {
        return currentScene;
    }

    public void setCurrentScene(Scene currentScene) {
        this.currentScene = currentScene;
    }

    public List<Scene> getScenes() {
        return scenes;
    }

    public void setScenes(List<Scene> scenes) {
        this.scenes = scenes;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }
}
