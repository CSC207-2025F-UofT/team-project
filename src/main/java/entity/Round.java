package entity;

import java.util.ArrayList;
import java.util.List;

public class Round {
    public static final int MAX_ATTEMPTS = GameRules.MAX_ATTEMPTS;

    private final AnswerCity target;
    private final List<City> guesses = new ArrayList<>();
    private RoundStatus status = RoundStatus.IN_PROGRESS;
    // Elliot should modify this status
    // If the guess is correct set the status to WON. If theres no attempts remaining
    // and the guess is incorrect, set it to LOST.

    public Round(AnswerCity target) {
        this.target = target;
    }

    public AnswerCity getTarget() {
        return target;
    }

    public List<City> getGuesses() {
        return List.copyOf(guesses);
    }

    public RoundStatus getStatus() {
        return status;
    }

    public int attemptsUsed() {
        return guesses.size();
    }

    public int getAttemptsLeft() {
        return MAX_ATTEMPTS - guesses.size();
    }

    public boolean isOver() {
        return status != RoundStatus.IN_PROGRESS;
    }

    public int getZoomStep() {
        return Math.min(guesses.size() + 1, MAX_ATTEMPTS);
    }
}
