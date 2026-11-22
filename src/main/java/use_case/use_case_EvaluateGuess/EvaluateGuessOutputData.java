package use_case.use_case_EvaluateGuess;

import entities.Game.CandleType;
import entities.Game.GameCondition;
import entities.Game.Guess;

import java.util.List;

public class EvaluateGuessOutputData {
    private List<CandleType> feedback;
    private int attemptsRemaining;
    private GameCondition gameCondition;
    private List<Guess> pastGuesses;

    public EvaluateGuessOutputData(List<CandleType> feedback, int attemptsRemaining, GameCondition gameCondition, List<Guess> pastGuesses) {
        this.feedback = feedback;
        this.attemptsRemaining = attemptsRemaining;
        this.gameCondition = gameCondition;
        this.pastGuesses = pastGuesses;
    }
    public List<CandleType> getFeedback() {
        return feedback;
    }
    public int getAttemptsRemaining() {
        return attemptsRemaining;
    }
    public GameCondition getGameCondition() {
        return gameCondition;
    }
    public List<Guess> getPastGuesses() {
        return pastGuesses;
    }
}
