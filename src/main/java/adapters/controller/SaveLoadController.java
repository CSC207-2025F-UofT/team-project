package adapters.controller;

import entity.Game;
import yinghaousecase.loadgame.LoadGameInputBoundary;
import yinghaousecase.savegame.SaveGameInputBoundary;

public class SaveLoadController {
    private final SaveGameInputBoundary saveInput;
    private final LoadGameInputBoundary loadInput;
    private Game currentGame;

    public SaveLoadController(SaveGameInputBoundary saveInput,
                              LoadGameInputBoundary loadInput) {
        this.saveInput = saveInput;
        this.loadInput = loadInput;
    }

    public void setCurrentGame(Game game) { this.currentGame = game; }

    public void onSaveResultClicked(String slotName) {
        saveInput.saveGame(currentGame, slotName);
    }

    public void onLoadResultClicked(String slotName) {
        loadInput.loadResult(slotName);
    }
}