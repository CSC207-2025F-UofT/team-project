package use_case.game;

import entity.ClickableObject;

/**
 * Input data for the Click Button use case.
 */
public class GameInputData {

    private final ClickableObject clickableObject;

    public GameInputData(ClickableObject clickableObject) {
        this.clickableObject = clickableObject;
    }

    public ClickableObject getClickableObject() {
        return clickableObject;
    }
}
