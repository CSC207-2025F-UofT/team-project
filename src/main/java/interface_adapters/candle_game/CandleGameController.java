package interface_adapters.candle_game;

import use_case.use_case_EvaluateGuess.EvaluateGuessInputBoundary;

public class CandleGameController {
    private final EvaluateGuessInputBoundary inputBoundary;

    public CandleGameController(EvaluateGuessInputBoundary inputBoundary) {
        this.inputBoundary = inputBoundary;
    }
    public void execute(String guess) {
        inputBoundary.execute(guess);
    }
}
