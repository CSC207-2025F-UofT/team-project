package use_case.update_game;

/** What the view needs to know after a tick. */
public class UpdateGameOutputData {
    private final double playerX;
    private final double playerY;
    private final int playerSize;

    public UpdateGameOutputData(double playerX, double playerY, int playerSize) {
        this.playerX = playerX;
        this.playerY = playerY;
        this.playerSize = playerSize;
    }

    public double getPlayerX() {
        return playerX;
    }

    public double getPlayerY() {
        return playerY;
    }

    public int getPlayerSize() {
        return playerSize;
    }
}
