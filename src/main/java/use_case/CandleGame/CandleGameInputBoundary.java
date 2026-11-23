package use_case.CandleGame;

public interface CandleGameInputBoundary {
    void initGame(); // initialize the game
    void quitGame(); // quits the game when user clicks to the quit button
    void showInstructions(); // show instructions when user clicks to the instructions button
    void timerExpired(); // input comes from the timer and ends the game, directing user to focus page
}


