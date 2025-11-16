package use_case.save;

import entity.Scene;
import entity.Player;

import java.util.List;

/**
 * Data access interface for the save use case.
 */
public interface SaveDataAccessInterface {

    /**
     * Returns the current scene.
     * @return the current scene
     */
    Scene getCurrentScene();
    List<Scene> getScenes();
    Player getPlayer();
}
