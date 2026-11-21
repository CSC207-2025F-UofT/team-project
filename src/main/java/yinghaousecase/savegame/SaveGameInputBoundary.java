package yinghaousecase.savegame;

import entity.Game;

public interface SaveGameInputBoundary {
    void saveGame(Game game, String slotName);
}
