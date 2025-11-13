package use_case;

import entity.MinesGame;

public interface MinesOutputBoundary {
    void presentBoard(MinesGame game, boolean safe);
}