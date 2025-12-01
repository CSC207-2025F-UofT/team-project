package interface_adapter.move;

import entity.Direction;
import entity.Monster;
import use_case.move.MoveInputBoundary;
import use_case.move.MoveInputData;

public class MoveController {
    private final MoveInputBoundary moveUseCaseInteractor;

    public MoveController(MoveInputBoundary moveUseCaseInteractor) {
        this.moveUseCaseInteractor = moveUseCaseInteractor;
    }

    public void execute(Direction direction) {
        final MoveInputData moveInputData = new MoveInputData(direction);
        moveUseCaseInteractor.execute(moveInputData);
    }

    public void switchToBattleView(Monster monster) {
        moveUseCaseInteractor.switchToBattleView(monster);
    }
}
