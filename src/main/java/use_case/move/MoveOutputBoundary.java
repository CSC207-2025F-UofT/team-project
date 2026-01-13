package use_case.move;

import entity.Monster;
import entity.User;

public interface MoveOutputBoundary {
    void present(MoveOutputData moveOutputData);

    void switchToBattleView(User user, Monster monster);
}
