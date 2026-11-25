package entity.board;

public record GridTransform (int x, int y, Direction direction){

    /// Returns a new GridTransform stepped in the Direction
    /// @return GridTransform with stepped x and/or y
    public GridTransform step() {
        return switch (direction) {
            case UP -> new GridTransform(x, y + 1, direction);
            case DOWN -> new GridTransform(x, y - 1, direction);
            case LEFT -> new GridTransform(x - 1, y, direction);
            case RIGHT -> new GridTransform(x + 1, y, direction);
            case UPRIGHT -> new GridTransform(x + 1, y + 1, direction);
            case UPLEFT -> new  GridTransform(x - 1, y + 1, direction);
            case DOWNLEFT -> new GridTransform(x - 1, y - 1, direction);
            case DOWNRIGHT -> new GridTransform(x + 1, y - 1, direction);
        };
    }

}
