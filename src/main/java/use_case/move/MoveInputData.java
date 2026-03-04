package use_case.move;

import entity.Direction;

public class MoveInputData {
    private Direction direction;

    public MoveInputData(Direction direction) {
        this.direction = direction;
    }

    public Direction getDirection() {
        return direction;
    }
}
