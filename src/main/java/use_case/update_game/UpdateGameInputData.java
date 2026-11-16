package use_case.update_game;

/** The player's input for one tick: which direction they want to move (-1, 0 or 1 on each axis). */
public class UpdateGameInputData {
    private final int moveX;
    private final int moveY;

    public UpdateGameInputData(int moveX, int moveY) {
        this.moveX = moveX;
        this.moveY = moveY;
    }

    public int getMoveX() {
        return moveX;
    }

    public int getMoveY() {
        return moveY;
    }
}
