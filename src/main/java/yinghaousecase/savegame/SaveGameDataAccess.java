package yinghaousecase.savegame;

import entity.GameResult;

import java.io.IOException;


public interface SaveGameDataAccess {
    void save(GameResult result, String slotName) throws IOException;
    GameResult load(String slotName) throws IOException, ClassNotFoundException;
}
