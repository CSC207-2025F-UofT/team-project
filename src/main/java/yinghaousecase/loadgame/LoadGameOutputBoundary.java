package yinghaousecase.loadgame;

import entity.GameResult;

public interface LoadGameOutputBoundary {
    void presentResultLoaded(GameResult result);
    void presentLoadFailure(String slotName, String reason);
}