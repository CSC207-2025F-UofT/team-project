import entity.Scene;
import entity.Player;

/**
 * Input data for the Save use case.
 */
public class SaveInputData {

    private final List<Scene> scenes;
    private final Scene currentScene;
    private final Player currentPlayerState;

    public SaveInputData(List<Scene> scenes, Scene currentScene, Player currentPlayerState) {
        this.scenes = scenes;
        this.currentScene = currentScene;
        this.currentPlayerState;
    }

    public List<Scene> getScenes() {
        return scenes;
    }

    public Scene getCurrentScene() {
        return currentScene;
    }

    public Player getCurrentPlayerState() {
        return currentPlayerState;
    }
}