package use_case.CandleGame;

public interface CandleGameOutputBoundary {
    void prepareGameView(CandleGameOutputData outputData); // after initializing finished, goes to candle game view and displays the screen
    void prepareBreakView(); // gets activated when quitGame() is called, return to Break view
    void prepareFocusView(); //when timer is expired, return to focus view
    void prepareInstructionsView(); // get triggered through instructions button

}
