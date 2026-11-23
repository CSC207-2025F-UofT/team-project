package entity;

import java.util.List;

/**
 * Game rule numbers shared by the use cases. Plain constants only.
 */
public final class GameRules {
    /** Guesses allowed per round. */
    public static final int MAX_ATTEMPTS = 5;

    /** Points for a win, by attempt: POINTS_BY_ATTEMPT.get(0) is a win on attempt 1 (100 points). */
    public static final List<Integer> POINTS_BY_ATTEMPT = List.of(100, 50, 25, 10, 0);

    /** Points taken away for a lost round. */
    public static final int LOSS_PENALTY = 25;

    private GameRules() {
    }
}
