package yinghaousecase.savegame;

import entity.Game;
import entity.GameResult;

public class SaveGameInteractor implements SaveGameInputBoundary {
    private final SaveGameDataAccess dataAccess;
    private final SaveGameOutputBoundary presenter;

    public SaveGameInteractor(SaveGameDataAccess dataAccess,
                              SaveGameOutputBoundary presenter) {
        this.dataAccess = dataAccess;
        this.presenter = presenter;
    }

    @Override
    public void saveGame(Game game, String slotName) {
        if (game == null) {
            presenter.presentSaveFailure(slotName, "No active game to save.");
            return;
        }
        if (slotName == null || slotName.isBlank()) {
            presenter.presentSaveFailure(slotName, "Save name cannot be empty.");
            return;
        }

        try {
            GameResult result = game.finish();
            dataAccess.save(result, slotName);
            presenter.presentSaveSuccess(slotName);
        } catch (Exception e) {
            presenter.presentSaveFailure(slotName, e.getMessage());
        }
    }
}
