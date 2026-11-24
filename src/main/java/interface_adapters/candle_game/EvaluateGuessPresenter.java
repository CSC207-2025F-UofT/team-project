package interface_adapters.candle_game;

import entities.Game.CandleType;
import entities.Game.GameCondition;
import entities.Game.Guess;
import use_case.use_case_EvaluateGuess.EvaluateGuessOutputBoundary;
import use_case.use_case_EvaluateGuess.EvaluateGuessOutputData;

import java.util.List;

public class EvaluateGuessPresenter implements EvaluateGuessOutputBoundary {
    private CandleGameViewModel candleGameViewModel;

    public EvaluateGuessPresenter(CandleGameViewModel candleGameViewModel) {
        this.candleGameViewModel = candleGameViewModel;
    }


    @Override
    public void prepareSuccessView(EvaluateGuessOutputData outputData) {
        List<CandleType> feedback = outputData.getFeedback();
        int attemptsRemaining = outputData.getAttemptsRemaining();
        GameCondition gameCondition = outputData.getGameCondition();
        List<Guess> pastGuesses = outputData.getPastGuesses();
        candleGameViewModel.setCandleFeedback(feedback);
        candleGameViewModel.setPastGuesses(pastGuesses);
        candleGameViewModel.setAttemptsRemaining(attemptsRemaining);
        candleGameViewModel.setGameCondition(gameCondition);
        candleGameViewModel.notifyStateChanged();
    }

//    @Override
//    public void prepareFailView(String errorMessage) {
//
//    }
}
