package data_access;
import use_case.earn_reward_coins.EarnRewardCoinsDataAccessInterface;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public class EarnRewardCoinsDataAccessObject
        implements EarnRewardCoinsDataAccessInterface{
    private final Map<String, ZoneId> timeZones = new HashMap<>();
    private final Map<String, Integer> balances = new HashMap<>();
    private final Map<String, Map<LocalDate, BigDecimal>> budgets =
            new HashMap<>();

    private final Map<String, Map<LocalDate, BigDecimal>> spending =
            new HashMap<>();

    private final Map<String, Set<LocalDate>> claimedDates =
            new HashMap<>();

    public synchronized void addUser(String userId, ZoneId timeZone) {
        if (timeZones.containsKey(userId)) {
            throw new IllegalArgumentException("User already exists.");
        }

        timeZones.put(userId, timeZone);
        balances.put(userId, 0);
        budgets.put(userId, new HashMap<>());
        spending.put(userId, new HashMap<>());
        claimedDates.put(userId, new HashSet<>());
    }

    public synchronized void setBudget(
            String userId, LocalDate date, BigDecimal amount) {
        requireUser(userId);

        if (amount.signum() < 0) {
            throw new IllegalArgumentException("Budget cannot be negative.");
        }

        budgets.get(userId).put(date, amount);
    }

    public synchronized void setTotalSpending(
            String userId, LocalDate date, BigDecimal amount) {
        requireUser(userId);

        if (amount.signum() < 0) {
            throw new IllegalArgumentException("Spending cannot be negative.");
        }

        spending.get(userId).put(date, amount);
    }

    @Override
    public synchronized ZoneId getUserTimeZone(String userId) {
        requireUser(userId);
        return timeZones.get(userId);
    }

    @Override
    public synchronized Optional<BigDecimal> getBudget(
            String userId, LocalDate date) {
        requireUser(userId);
        return Optional.ofNullable(budgets.get(userId).get(date));
    }

    @Override
    public synchronized BigDecimal getTotalSpending(
            String userId, LocalDate date) {
        requireUser(userId);
        return spending.get(userId).getOrDefault(date, BigDecimal.ZERO);
    }

    @Override
    public synchronized boolean hasClaimedReward(
            String userId, LocalDate rewardDate) {
        requireUser(userId);
        return claimedDates.get(userId).contains(rewardDate);
    }

    @Override
    public synchronized int claimReward(
            String userId, LocalDate rewardDate, int coins) {
        requireUser(userId);

        if (coins <= 0) {
            throw new IllegalArgumentException("Coins must be positive.");
        }

        Set<LocalDate> userClaims = claimedDates.get(userId);

        if (userClaims.contains(rewardDate)) {
            throw new IllegalStateException("Reward already claimed.");
        }

        // Calculate before changing either stored value.
        int newBalance = Math.addExact(balances.get(userId), coins);

        balances.put(userId, newBalance);
        userClaims.add(rewardDate);

        return newBalance;
    }

    private void requireUser(String userId) {
        if (!timeZones.containsKey(userId)) {
            throw new IllegalArgumentException("Unknown user: " + userId);
        }
    }
}
