package entity;

import entity.GuessRecord;

import java.util.List;

public class RoundRecord {
    private final int roundNumber;
    private final String questionText;
    private final List<GuessRecord> guesses;

    // TODO: Drop the guesses parameter and set this.guesses = new ArrayList<>() inside the constructor.
    //  Right now, if a caller passes List.of() (an immutable list), addGuess() throws UnsupportedOperationException.
    public RoundRecord(int roundNumber, String questionText,  List<GuessRecord> guesses) {
        this.roundNumber = roundNumber;
        this.questionText = questionText;
        this.guesses = guesses;
    }

    public void addGuess(GuessRecord guess) {
        guesses.add(guess);
    }

    public int getRoundNumber() {
        return roundNumber;
    }
    public String getQuestionText() {
        return questionText;
    }
    public List<GuessRecord> getGuesses() {
        return guesses;
    }
}