package entities;


import java.util.List;

/**
 * Represents the guesses player attempted during the game and their feedback
 */

public class Guess {
    private final String guessedCode;  // holds the guessed code
    private final List<CandleType> feedback; // holds the feedbacks of the guessed code


    public Guess(String guessedCode, List<CandleType> feedback) {
        this.guessedCode = guessedCode;
        this.feedback = List<CandleType> feedback;
    }

    /**
     * Returns the guessed code.
     *
     * @return the guessed code .
     */
    public String getGuessedCode() {
        return guessedCode;
    }


    /**
     * Returns the feedback list.
     *
     * @return the feedback list.
     */
    public List<CandleType> getFeedback() {
        return feedback;
    }
}
