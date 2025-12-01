package use_case.move;

import entity.Monster;

public interface MoveInputBoundary {
    void execute(MoveInputData moveInputData);

    void switchToBattleView(Monster monster);
}
