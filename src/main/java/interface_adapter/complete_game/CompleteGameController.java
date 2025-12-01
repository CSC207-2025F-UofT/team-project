package interface_adapter.complete_game;
import use_case.CompleteGame.CompleteGameInputBoundary;
import use_case.CompleteGame.CompleteGameInputData;


public class CompleteGameController {
    private final CompleteGameInputBoundary interactor;

    public CompleteGameController(CompleteGameInputBoundary interactor) {
        this.interactor = interactor;
    }

    public void completeGame() {
        CompleteGameInputData input = new CompleteGameInputData(true);
        interactor.execute(input);
    }
}
