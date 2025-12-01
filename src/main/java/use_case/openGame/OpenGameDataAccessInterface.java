package use_case.openGame;

import entity.GameState;

public interface OpenGameDataAccessInterface {
    GameState loadGame();
    void saveGame(GameState state);
    boolean saveFileExists();
    void deleteSaveFile();

}
