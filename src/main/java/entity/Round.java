package entity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;

/**
 * One round of the game: a target city, the guesses made so far, and whether
 * the round is still going, won or lost.
 * Elliot adds the methods for recording a guess and marking the round won or lost.
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

    public RoundStatus getStatus() {
        return status;
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
