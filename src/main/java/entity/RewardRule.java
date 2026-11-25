package entity;

import java.math.BigDecimal;

public class RewardRule {
    private final int coinsAwarded;

    public RewardRule(int coinsAwarded) {
        if (coinsAwarded <= 0) {
            throw new IllegalArgumentException(
                    "Reward amount must be positive.");
        }
        this.coinsAwarded = coinsAwarded;
    }

    public boolean isEligible(
            BigDecimal budget, BigDecimal spending) {
        return budget != null
                && spending != null
                && budget.signum() > 0
                && spending.signum() >= 0
                && spending.compareTo(budget) <= 0;
    }

    public int getCoinsAwarded() {
        return coinsAwarded;
    }
}