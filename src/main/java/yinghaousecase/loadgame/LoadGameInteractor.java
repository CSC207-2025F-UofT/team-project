package yinghaousecase.loadgame;

import entity.GameResult;
import yinghaousecase.savegame.SaveGameDataAccess;

public class LoadGameInteractor implements LoadGameInputBoundary {
    private final SaveGameDataAccess dataAccess;
    private final LoadGameOutputBoundary presenter;

    public LoadGameInteractor(SaveGameDataAccess dataAccess,
                              LoadGameOutputBoundary presenter) {
        this.dataAccess = dataAccess;
        this.presenter = presenter;
    }

    @Override
    public void loadResult(String slotName) {
        if (slotName == null || slotName.isBlank()) {
            presenter.presentLoadFailure(slotName, "Load name cannot be empty.");
            return;
        }
        try {
            GameResult result = dataAccess.load(slotName);
            presenter.presentResultLoaded(result);
        } catch (Exception e) {
            presenter.presentLoadFailure(slotName, e.getMessage());
        }
    }
}