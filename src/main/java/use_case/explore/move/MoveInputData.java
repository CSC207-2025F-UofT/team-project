package use_case.explore.move;

import entity.Direction;

/**
 * Input for the Move use case: the direction the player chose.
 */
public class MoveInputData {
    /** The chosen direction. */
    private final Direction direction;

    /**
     * Creates the input data.
     *
     * @param direction the chosen direction
     */
    public MoveInputData(Direction direction) {
        this.direction = direction;
    }

    /** @return the chosen direction */
    public Direction getDirection() {
        return direction;
    }
}
