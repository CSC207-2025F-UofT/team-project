package entities.Game;


import java.util.ArrayList;
import java.util.List;

/**
 * Represents the guesses player attempted during the game and their feedback
 */

public class Guess {
    private final String guessedCode;  // holds the guessed code
    private final List<CandleType> feedback; // holds the feedbacks of the guessed code


    public Guess(String guessedCode, String secretCode) {
        this.guessedCode = guessedCode;
        this.feedback = generateFeedback(secretCode);
    }

    /**
     * Generates the feedback of guess.
     *
     * @return the feedback list .
     */
    public List<CandleType> generateFeedback(String secretCode) {
        List<CandleType> feedback = new ArrayList<>();
        for (int i = 0; i < getGuessedCode().length(); i++ ) {
            char digit = getGuessedCode().charAt(i);
            if (digit == secretCode.charAt(i)) {
                feedback.add(CandleType.BRIGHT);
            } else if (secretCode.contains(String.valueOf(digit))) {
                feedback.add(CandleType.DIM);
            } else {
                feedback.add(CandleType.SNUFF);
            }
        }
            return feedback;

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
