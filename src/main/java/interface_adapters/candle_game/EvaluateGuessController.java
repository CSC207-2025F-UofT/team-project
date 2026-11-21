package interface_adapters.candle_game;

import entities.Game.CandleGame;
import use_case.use_case_EvaluateGuess.EvaluateGuessInputBoundary;
import use_case.use_case_EvaluateGuess.EvaluateGuessInputData;

public class EvaluateGuessController {
    private final EvaluateGuessInputBoundary inputBoundary;

    public EvaluateGuessController(EvaluateGuessInputBoundary inputBoundary) {
        this.inputBoundary = inputBoundary;
    }
    public void execute (String guess, CandleGame game) {
        EvaluateGuessInputData inputData = new EvaluateGuessInputData(guess, game);
        inputBoundary.execute(inputData);
    }
}
