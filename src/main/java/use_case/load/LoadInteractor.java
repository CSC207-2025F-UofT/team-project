package use_case.load;

import java.util.Map;

import entity.Scene;
import entity.Player;

/**
 * The save interactor.
 */
public class LoadInteractor implements LoadInputBoundary {
    private final LoadDataAccessInterface loadDataAccessObject;
    private final LoadOutputBoundary presenter;

    public LoadInteractor(LoadDataAccessInterface loadDataAccessObject, LoadOutputBoundary presenter) {
        this.loadDataAccessObject = loadDataAccessObject;
        this.presenter = presenter;
    }

    @Override
    public void execute() {
        loadDataAccessObject.loadGame("save.json");

        Scene currentScene = loadDataAccessObject.getCurrentScene();
        LoadOutputData loadOutputData = new LoadOutputData();
        loadOutputData.setBackgroundImage(currentScene.getImage());
        loadOutputData.setClickableObjects(currentScene.getObjects());
        presenter.switchToLoadView(loadOutputData);
    }
}
