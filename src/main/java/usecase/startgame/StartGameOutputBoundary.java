package usecase.startgame;

import entity.Game;

public interface StartGameOutputBoundary {
    void presentGameStarted(Game game);
    void presentStartFailure(String reason);
}
