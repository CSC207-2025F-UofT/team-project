package interface_adapter.new_game;

import use_case.new_game.NewGameInputBoundary;
import use_case.new_game.NewGameInputData;

/**
 * Turns the "New Game" button press into a call on the use case.
 */
public class NewGameController {

    private final NewGameInputBoundary newGameInteractor;

    public NewGameController(NewGameInputBoundary newGameInteractor) {
        this.newGameInteractor = newGameInteractor;
    }

    public void execute() {
        newGameInteractor.execute(new NewGameInputData());
    }
}
