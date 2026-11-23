package use_case.load_scene;

import entity.Scene;
import entity.Terrain;
import use_case.scene.SceneFileException;
import use_case.scene.SceneViewData.HeightGrid;

public interface LoadSceneDataAccessInterface {
    Scene load(String filePath) throws SceneFileException;

    HeightGrid getHeightGrid(Terrain terrain) throws SceneFileException;

    void replaceCurrentScene(Scene scene);
}
