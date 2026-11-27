package use_case.playcard;

import entity.Game;

public interface PlayCardDataAccessInterface {
    Game getCurrentGame();

    void saveGame(Game game);
}