package entity;

import java.util.List;

public class NormalScoring implements ScoringStrategy {
    private static final List<Integer> POINTS_BY_ATTEMPT = List.of(100, 50, 25, 10, 0);
    private static final int LOSS_PENALTY = 25;

    @Override
    public int pointsFor(Round round) {
        if (round.getStatus() == RoundStatus.WON) {
            return POINTS_BY_ATTEMPT.get(round.attemptsUsed() - 1);
        }
        if (round.getStatus() == RoundStatus.LOST) {
            return -LOSS_PENALTY;
        }
        return 0;
    }
}
