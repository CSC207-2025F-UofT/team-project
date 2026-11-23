package use_case.save_scene;

import entity.Scene;
import use_case.scene.SceneFileException;

public interface SaveSceneDataAccessInterface {
    Scene getCurrentScene();

    /** Returns false if the selected file exists and the user has not approved replacement. */
    boolean save(String filePath, Scene scene, boolean overwrite) throws SceneFileException;
}
