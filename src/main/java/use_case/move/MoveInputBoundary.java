package use_case.move;

import entity.Monster;

public interface MoveInputBoundary {
    void execute(MoveInputData moveInputData);

    void updateGame();

    void switchToBattleView(Monster monster);
}
