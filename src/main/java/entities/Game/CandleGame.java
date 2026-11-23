package entities.Game;

import java.util.ArrayList;
import java.util.List;

public class CandleGame {

    // Refer to the API documentation for the meaning of these fields.
    private final String secretCode;  // holds the secret code
    private final int maxTries; // holds the max tries user can make
    private GameCondition gameCondition; // If the game was Won, Lost, still going
    private List<Guess> guesses;



    public CandleGame(String secretCode, int maxTries) {
        this.secretCode = secretCode;
        this.maxTries = maxTries;
        this.gameCondition = GameCondition.ON_GOING;
        this.guesses = new ArrayList<>(); // until we assign a guess, it will be an empty list
    }

    /**
     * Returns the secret code.
     * @return the secret code .
     */
    public String getSecretCode() {
        return secretCode;
    }

    /**
     * Returns the Max attempt.
     * @return the Max attempt.
     */
    public int getMaxTries() {
        return maxTries;
    }

    /**
     * Returns how many tries were attempted
     * @return how many tries were attempted
     */
    public int getAttemptedTries() {
        return guesses.size();
    }

    public void setGameCondition(GameCondition gameCondition) {
        this.gameCondition = gameCondition;
    }

    /**
     * Returns the current condition of the game
     * @return the current condition of the game
     */
    public GameCondition getGameCondition() {
        return gameCondition;
    }

    /**
     * Returns the list of all guesses
     * @return the list of all guesses
     */
    public List<Guess> getGuesses() {

        return List.copyOf(guesses);
    }

    public int getAttemptsRemaining() {
        return maxTries-guesses.size();
    }

    public void addGuess(Guess guess) {
        if (gameCondition != GameCondition.ON_GOING ){
            throw new IllegalStateException("Game is over."); }
        if (getAttemptsRemaining() <= 0) {
            throw new IllegalStateException("Game is over.");
        }
        guesses.add(guess);
        updateGameCondition();
    }

    public void updateGameCondition() {
        Guess recentGuess = guesses.get(guesses.size()-1);
        List<CandleType> feedback = recentGuess.getFeedback();
        boolean isCorrect = true;
        for (int i = 0; i<feedback.size(); i++) {
            if (feedback.get(i) != CandleType.BRIGHT) {
                isCorrect = false;
            }
        }
        if ((!isCorrect) && (getMaxTries() == guesses.size())) {
            gameCondition = GameCondition.LOSS;
        }
        else if ((!isCorrect) && (getAttemptsRemaining() > 0)) {
            gameCondition = GameCondition.ON_GOING;
        }
        else {
            gameCondition = GameCondition.WON;
        }
    }






}

