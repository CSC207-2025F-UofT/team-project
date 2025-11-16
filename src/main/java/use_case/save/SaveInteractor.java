package use_case.save;

import java.util.List;
import entity.Scene;
import entity.Player;
import use_case.save.SaveInputBoundary;
import use_case.save.SaveOutputData;
// import use_case.game.GameOutputBoundary;


/**
 * The save interactor.
 */
public class SaveInteractor implements SaveInputBoundary {
    private final SaveDataAccessInterface saveDataAccessObject;
    // private final SaveOutputBoundary presenter;

    public SaveInteractor(SaveDataAccessInterface saveDataAccessInterface,
                                      SwitchToGameViewOutputBoundary switchToGameViewOutputBoundary) {
        this.saveDataAccessObject = saveDataAccessInterface;
        // .presenter = switchToGameViewOutputBoundary;
    }

    @Override
    public void execute() {
        Scene currentScene = saveDataAccessObject.getCurrentScene();
        List<Scene> scenes = saveDataAccessObject.getScenes();
        Player player = saveDataAccessObject.getPlayer();

        SaveOutputData saveOutputData = new SaveOutputData();
        saveOutputData.setPlayer(player);
        saveOutputData.setScenes(scenes);
        saveOutputData.setCurrentScene(currentScene);
        presenter.switchToGameView(gameOutputData);
    }
}
