package use_case.load_scene;

import entity.Scene;
import use_case.scene.SceneFileException;
import use_case.scene.SceneFileValidator;
import use_case.scene.SceneViewData;

public final class LoadSceneInteractor implements LoadSceneInputBoundary {
    private final LoadSceneDataAccessInterface dataAccess;
    private final LoadSceneOutputBoundary presenter;

    public LoadSceneInteractor(LoadSceneDataAccessInterface dataAccess, LoadSceneOutputBoundary presenter) {
        this.dataAccess = dataAccess;
        this.presenter = presenter;
    }

    @Override
    public void execute(LoadSceneInputData input) {
        Scene loadedScene;
        SceneViewData viewData;
        try {
            loadedScene = dataAccess.load(input.getFilePath());
            SceneFileValidator.checkBallPresets(loadedScene);
            viewData = SceneViewData.from(loadedScene, dataAccess.getHeightGrid(loadedScene.getTerrain()));
        } catch (SceneFileException e) {
            presenter.prepareFailView(e.getMessage());
            return;
        }

        dataAccess.replaceCurrentScene(loadedScene);
        presenter.prepareSuccessView(new LoadSceneOutputData(input.getFileName(), viewData));
    }
}
