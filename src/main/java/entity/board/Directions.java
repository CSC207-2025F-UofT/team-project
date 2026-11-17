package entity.board;

public class Directions {
    public static Direction reverse(Direction direction) {
        return switch (direction) {
            case UP -> Direction.DOWN;
            case DOWN -> Direction.UP;
            case LEFT -> Direction.RIGHT;
            case RIGHT -> Direction.LEFT;
            case UPRIGHT -> Direction.DOWNLEFT;
            case UPLEFT -> Direction.DOWNRIGHT;
            case DOWNRIGHT -> Direction.UPLEFT;
            case DOWNLEFT -> Direction.UPRIGHT;
        };
    }

    public static boolean isDiagonal(Direction direction) {
        return direction == Direction.DOWNLEFT || direction == Direction.UPRIGHT ||
                direction == Direction.UPLEFT || direction == Direction.DOWNRIGHT;
    }
}
