package use_case.earn_reward_coins;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.LocalDate;
import java.util.Optional;

public class EarnRewardCoinsStatusInteractor
        implements EarnRewardCoinsStatusInputBoundary {

    private final EarnRewardCoinsDataAccessInterface dataAccess;
    private final EarnRewardCoinsStatusOutputBoundary presenter;
    private final Clock clock;

    public EarnRewardCoinsStatusInteractor(
            EarnRewardCoinsDataAccessInterface dataAccess,
            EarnRewardCoinsStatusOutputBoundary presenter,
            Clock clock) {
        this.dataAccess = dataAccess;
        this.presenter = presenter;
        this.clock = clock;
    }

    @Override
    public void execute(String userId) {
        LocalDate yesterday = LocalDate.now(
                        clock.withZone(dataAccess.getUserTimeZone(userId)))
                .minusDays(1);

        int balance = dataAccess.getCoinBalance(userId);
        boolean claimed = dataAccess.hasClaimedReward(userId, yesterday);

        Optional<BigDecimal> budget =
                dataAccess.getBudget(userId, yesterday);

        BigDecimal spending =
                dataAccess.getTotalSpending(userId, yesterday);

        boolean eligible = !claimed
                && budget.isPresent()
                && budget.get().signum() > 0
                && spending.compareTo(budget.get()) < 0;

        String message;

        if (claimed) {
            message = "You already claimed yesterday's reward.";
        } else if (budget.isEmpty() || budget.get().signum() <= 0) {
            message = "You did not have a positive budget yesterday.";
        } else if (!eligible) {
            message = "Your spending yesterday was not under budget.";
        } else {
            message = "You stayed under budget yesterday. Claim your coins!";
        }

        presenter.presentStatus(balance, eligible, claimed, message);
    }
}