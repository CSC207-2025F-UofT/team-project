package interface_adapter.game;

/** An immutable snapshot of everything the view draws. A new one is made every tick. */
public class GameState {
    private final int playerX;
    private final int playerY;
    private final int playerSize;

    public GameState(int playerX, int playerY, int playerSize) {
        this.playerX = playerX;
        this.playerY = playerY;
        this.playerSize = playerSize;
    }

    public int getPlayerX() {
        return playerX;
    }

    public int getPlayerY() {
        return playerY;
    }

    public int getPlayerSize() {
        return playerSize;
    }
}
