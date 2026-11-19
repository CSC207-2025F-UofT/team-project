package entity;

/**
 * Game rule numbers shared by the use cases. Plain constants only.
 */
public final class GameRules {
    /** Guesses allowed per round. */
    public static final int MAX_ATTEMPTS = 5;

    /** Points for a win, indexed by attempt number - 1 (win on attempt 1 = 100 points). */
    private static final int[] POINTS_BY_ATTEMPT = {100, 50, 25, 10, 0};

    /** Points taken away for a lost round. */
    public static final int LOSS_PENALTY = 25;

    private GameRules() {
    }

    /** Points for winning on the given attempt (1 to 5). */
    public static int pointsForWinOnAttempt(int attempt) {
        return POINTS_BY_ATTEMPT[attempt - 1];
    }
}
