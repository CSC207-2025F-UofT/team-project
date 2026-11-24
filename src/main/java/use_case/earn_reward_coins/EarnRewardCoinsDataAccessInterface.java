package use_case.earn_reward_coins;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Optional;

public interface EarnRewardCoinsDataAccessInterface {
    ZoneId getUserTimeZone(String userId);
    // Empty if no budget was set for that date.
    Optional<BigDecimal> getBudget(String userId, LocalDate date);
    // Return BigDecimal.ZERO if there were no expenses.
    BigDecimal getTotalSpending(String userId, LocalDate date);
    boolean hasClaimedReward(String userId, LocalDate rewardDate);
    int claimReward(String userId, LocalDate rewardDate, int coins);
    int getCoinBalance(String userId);
}
