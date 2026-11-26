package entity;

import java.util.LinkedHashSet;

/**
 * One round of the game: a target city, the guesses made so far, and whether
 * the round is still going, won or lost.
*/

public class Round {
    public static final int MAX_ATTEMPTS = GameRules.MAX_ATTEMPTS;

    private final AnswerCity target;
    private final LinkedHashSet<City> guesses = new LinkedHashSet<City>();
    private RoundStatus status = RoundStatus.IN_PROGRESS;

    public Round(AnswerCity target) {
        this.target = target;
    }

    public AnswerCity getTarget() {
        return target;
    }

    // Guesses made so far
    public LinkedHashSet<City> getGuesses() {
        return guesses;
    }

    // Accumulate previously guessed cities
    public void addGuess(City city) {
        if (! this.isOver()) {
            guesses.add(city);
        }
    }

    public RoundStatus getStatus() {
        return status;
    }

    public int attemptsRemaining() {
        return MAX_ATTEMPTS - guesses.size();
    }

    public boolean isOver() {
        return status != RoundStatus.IN_PROGRESS;
    }

    public void endRound(boolean win) {
        if (win) {
            status = RoundStatus.WON;
        } else {
            status = RoundStatus.LOST;
        }
    }

    /**
     * Which image to show, as a plain step from 1 (most zoomed in) to 5 (most zoomed out).
     * The real Mapbox zoom level for each step lives in MapboxImageDataAccessObject.
     */
    public int getZoomStep() {
        return Math.min(guesses.size() + 1, MAX_ATTEMPTS);
    }

    public int attemptsUsed() { return guesses.size();}
}
