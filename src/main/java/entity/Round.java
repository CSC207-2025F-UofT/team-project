package entity;

public class Round {
    public static final int MAX_ATTEMPTS = 5;
    public static final int START_ZOOM = 17;
    public static final int ZOOM_STEP = 1;

    private final AnswerCity answerCity;
    private int wrongGuesses = 0;
    private boolean roundOver = false;

    public Round(AnswerCity answerCity) {
        this.answerCity = answerCity;
    }

    public AnswerCity getAnswer() {
        return answerCity;
    }

    public int getWrongGuesses() {
        return wrongGuesses;
    }

    public boolean isRoundOver() {
        return roundOver;
    }

    public int getZoom() {
        return START_ZOOM - wrongGuesses * ZOOM_STEP;
    }

    public int getAttemptsLeft() {
        return MAX_ATTEMPTS - wrongGuesses;
    }

    public void addWrongGuess() {
        wrongGuesses++;
        if (wrongGuesses >= MAX_ATTEMPTS) {
            roundOver = true;
        }
    }

    public void endRound() {
        roundOver = true;
    }
}