package interface_adapter.game;

import use_case.update_game.UpdateGameInputBoundary;
import use_case.update_game.UpdateGameInputData;

/** Turns raw key state into a use-case request. Called once per tick by the game loop. */
public class GameController {
    private final UpdateGameInputBoundary updateGame;

    public GameController(UpdateGameInputBoundary updateGame) {
        this.updateGame = updateGame;
    }

    public void tick(boolean up, boolean down, boolean left, boolean right) {
        int moveX = (right ? 1 : 0) - (left ? 1 : 0);
        int moveY = (down ? 1 : 0) - (up ? 1 : 0);   // Swing's y grows downward
        updateGame.execute(new UpdateGameInputData(moveX, moveY));
    }
}
