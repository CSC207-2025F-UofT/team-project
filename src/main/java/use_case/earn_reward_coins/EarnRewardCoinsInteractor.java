package use_case.earn_reward_coins;
import java.math.BigDecimal;
import java.time.Clock;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Optional;

public class EarnRewardCoinsInteractor implements EarnRewardCoinsInputBoundary{
    private final EarnRewardCoinsDataAccessInterface dataAccess;
    private final EarnRewardCoinsOutputBoundary presenter;
    private final int rewardAmount;
    private final Clock clock;

    public EarnRewardCoinsInteractor(
            EarnRewardCoinsDataAccessInterface dataAccess,
            EarnRewardCoinsOutputBoundary presenter,
            int rewardAmount,
            Clock clock) {
        if (rewardAmount <= 0) {
            throw new IllegalArgumentException("Reward amount must be positive.");
        }
        this.dataAccess = dataAccess;
        this.presenter = presenter;
        this.rewardAmount = rewardAmount;
        this.clock = clock;
    }

        @Override
    public void execute(EarnRewardCoinsInputData inputData){
        String userId = inputData.getUserId();

        ZoneId userTimeZone = dataAccess.getUserTimeZone(userId);
        LocalDate yesterday = LocalDate.now(clock.withZone(userTimeZone)).minusDays(1);
        Optional<BigDecimal> budget = dataAccess.getBudget(userId, yesterday);
        // Check whether the conditions are false to claim reward
        if (budget.isEmpty() || budget.get().compareTo(BigDecimal.ZERO) <= 0) {
            presenter.prepareFailView(
                    "You did not have a positive budget yesterday");
            return;
        }
        BigDecimal spending = dataAccess.getTotalSpending(userId, yesterday);
        if (spending.compareTo(budget.get()) >= 0){
            presenter.prepareFailView(
                    "Your spending yesterday was not under budget.");
            return;
        }
        if (dataAccess.hasClaimedReward(userId, yesterday)) {
            presenter.prepareFailView(
                    "You already claimed yesterday's reward.");
            return;
        }
        // if user have already claimed that
        int newBalance;
        try{
            newBalance = dataAccess.claimReward(userId, yesterday, rewardAmount);
        } catch (IllegalStateException exception){
            presenter.prepareFailView(
                    "You already claimed yesterday's reward");
            return;
        }
        // Suppose the conditions are all true, prepare success view
        EarnRewardCoinsOutputData outputData =
                new EarnRewardCoinsOutputData(rewardAmount, newBalance);
        presenter.prepareSuccessView(outputData);
    }
}
