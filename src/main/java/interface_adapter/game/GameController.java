package interface_adapter.game;

import entity.ClickableObject;
import use_case.game.GameInputBoundary;
import use_case.game.GameInputData;

/**
 * Controller for the Game View.
 */
public class GameController {

    private final GameInputBoundary clickButtonInteractor;

    public GameController(GameInputBoundary clickButtonInteractor) {
        this.clickButtonInteractor = clickButtonInteractor;
    }

    /**
     * Click a clickable object
     */
    public void click(ClickableObject clickableObject) {
        clickButtonInteractor.execute(new GameInputData(clickableObject));
    }
}
