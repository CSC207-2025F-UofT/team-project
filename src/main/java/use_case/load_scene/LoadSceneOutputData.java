package use_case.load_scene;

import use_case.scene.SceneViewData;

public final class LoadSceneOutputData {
    private final String fileName;
    private final SceneViewData scene;

    public LoadSceneOutputData(String fileName, SceneViewData scene) {
        this.fileName = fileName;
        this.scene = scene;
    }

    public String getFileName() {
        return fileName;
    }

    public SceneViewData getScene() {
        return scene;
    }
}
