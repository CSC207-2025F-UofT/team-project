package interface_adapter.continue_game;

import use_case.continue_game.ContinueGameInputBoundary;

/**
 * Controller for continuing a saved game.
 */
public class ContinueGameController {
    private final ContinueGameInputBoundary interactor;

    public ContinueGameController(ContinueGameInputBoundary interactor) {
        this.interactor = interactor;
    }

    public void execute() {
        interactor.execute();
    }
}
