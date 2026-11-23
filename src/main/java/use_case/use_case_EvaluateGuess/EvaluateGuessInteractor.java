package use_case.use_case_EvaluateGuess;

import entities.Game.CandleGame;
import entities.Game.CandleType;
import entities.Game.GameCondition;
import entities.Game.Guess;

import java.util.List;

public class EvaluateGuessInteractor implements EvaluateGuessInputBoundary{
    private final EvaluateGuessOutputBoundary outputBoundary;


    public EvaluateGuessInteractor(EvaluateGuessOutputBoundary outputBoundary) {
        this.outputBoundary = outputBoundary;
    }

    @Override
    public void execute(EvaluateGuessInputData inputData) {
        String guess = inputData.getGuess();
        CandleGame game = inputData.getGame();
        String secretCode  = game.getSecretCode();
        Guess newGuess = new Guess(guess, secretCode);
        game.addGuess(newGuess);
        game.updateGameCondition();
        List<CandleType> feedback = newGuess.getFeedback();
        int attemptsRemaining = game.getAttemptsRemaining();
        GameCondition gameCondition = game.getGameCondition();
        List<Guess> pastguesses = game.getGuesses();
        EvaluateGuessOutputData outputData = new EvaluateGuessOutputData(feedback, attemptsRemaining, gameCondition, pastguesses);
        outputBoundary.prepareSuccessView(outputData);
    }
}
