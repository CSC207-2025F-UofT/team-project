package interface_adapters.candle_game;

import use_case.CandleGame.CandleGameOutputData;
import use_case.CandleGame.CandleGameOutputBoundary;
import interface_adapters.candle_game.CandleGameViewModel;



public class CandleGamePresenter implements CandleGameOutputBoundary {

    @Override
    public void prepareGameView(CandleGameOutputData outputData) {
        // TODO prepare the game
    }

    @Override
    public void prepareBreakView() {
        // TODO quit the game and go back to the break screen

    }

    @Override
    public void prepareFocusView() {
        // TODO go back to the focus screen
    }

    @Override
    public void prepareInstructionsView() {
        // TODO get the instruction screen
    }

    ;




}
