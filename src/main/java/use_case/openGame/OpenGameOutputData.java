package use_case.openGame;

import entity.GameState;

public class OpenGameOutputData {
    private final String message;
    private final GameState gameState;
    private final boolean isNewGame;

    public OpenGameOutputData(String message, GameState gameState, boolean isNewGame) {
        this.message = message;
        this.gameState = gameState;
        this.isNewGame = isNewGame;
    }

    public String getMessage() {
        return message;
    }

    public GameState getGameState() {
        return gameState;
    }

    public boolean isNewGame() {
        return isNewGame;
    }
}
