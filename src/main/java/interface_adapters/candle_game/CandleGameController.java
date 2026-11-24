package interface_adapters.candle_game;

import use_case.use_case_EvaluateGuess.EvaluateGuessInputBoundary;
import use_case.CandleGame.CandleGameInputBoundary;


public class CandleGameController {
    private final CandleGameInputBoundary candleGameInputBoundary;

    public CandleGameController( CandleGameInputBoundary candleGameInputBoundary) {
        this.candleGameInputBoundary = candleGameInputBoundary;
    }

    // Candle Game Use Case:
    public void initGame() {candleGameInputBoundary.initGame();};
    public void quitGame(){candleGameInputBoundary.quitGame();};
    public void showInstructions(){candleGameInputBoundary.showInstructions();};
    public void timerExpired(){candleGameInputBoundary.timerExpired();}

}
