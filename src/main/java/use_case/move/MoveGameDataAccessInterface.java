package use_case.move;

import entity.AdventureGame;

public interface MoveGameDataAccessInterface {
    AdventureGame getGame();

    void saveGame(AdventureGame game);
}
