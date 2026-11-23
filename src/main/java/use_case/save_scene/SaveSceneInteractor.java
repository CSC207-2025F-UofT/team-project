package use_case.save_scene;

import entity.Scene;
import use_case.scene.SceneFileException;
import use_case.scene.SceneFileValidator;

public final class SaveSceneInteractor implements SaveSceneInputBoundary {
    private final SaveSceneDataAccessInterface dataAccess;
    private final SaveSceneOutputBoundary presenter;

    public SaveSceneInteractor(SaveSceneDataAccessInterface dataAccess, SaveSceneOutputBoundary presenter) {
        this.dataAccess = dataAccess;
        this.presenter = presenter;
    }

    @Override
    public void execute(SaveSceneInputData input) {
        Scene scene = dataAccess.getCurrentScene();
        if (scene == null || scene.getTerrain() == null) {
            presenter.prepareFailView("Add terrain before saving the scene.");
            return;
        }

        boolean saved;
        try {
            SceneFileValidator.checkBallPresets(scene);
            saved = dataAccess.save(input.getFilePath(), scene, input.mayOverwrite());
        } catch (SceneFileException e) {
            presenter.prepareFailView(e.getMessage());
            return;
        }
        SaveSceneOutputData output = new SaveSceneOutputData(input.getFilePath(), input.getFileName());
        if (saved) {
            presenter.prepareSuccessView(output);
        } else {
            presenter.prepareOverwriteView(output);
        }
    }
}
