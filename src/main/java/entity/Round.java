package entity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * One round of the game: a target city, the guesses made so far, and whether
 * the round is still going, won or lost.
 * Elliot's Submit Guess interactor decides when to set WON or LOST.
 */
public class Round {
    public static final int MAX_ATTEMPTS = GameRules.MAX_ATTEMPTS;

    private final AnswerCity target;
    private final List<City> guesses = new ArrayList<>();
    private RoundStatus status = RoundStatus.IN_PROGRESS;

    public Round(AnswerCity target) {
        this.target = target;
    }

    public AnswerCity getTarget() {
        return target;
    }

    /** The guesses made so far, oldest first. Read-only; use addGuess to add one. */
    public List<City> getGuesses() {
        return Collections.unmodifiableList(guesses);
    }

    public RoundStatus getStatus() {
        return status;
    }

    public void setStatus(RoundStatus status) {
        this.status = status;
    }

    /** Records a guess and uses up one attempt. Does not decide won/lost. */
    public void addGuess(City guess) {
        guesses.add(guess);
    }

    public int attemptsUsed() {
        return guesses.size();
    }

    public int attemptsRemaining() {
        return MAX_ATTEMPTS - guesses.size();
    }

    public boolean isOver() {
        return status != RoundStatus.IN_PROGRESS;
    }

    /**
     * Which image to show, as a plain step from 1 (most zoomed in) to 5 (most zoomed out).
     * The real Mapbox zoom level for each step lives in MapboxImageDataAccessObject.
     */
    public int getZoomStep() {
        return Math.min(guesses.size() + 1, MAX_ATTEMPTS);
    }
}
