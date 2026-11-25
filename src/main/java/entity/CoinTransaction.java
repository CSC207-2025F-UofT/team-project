package entity;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Objects;

public class CoinTransaction {
    private final String transactionId;
    private final String userId;
    private final int coinsEarned;
    private final LocalDate rewardDate;
    private final Instant createdAt;

    public CoinTransaction(
            String transactionId,
            String userId,
            int coinsEarned,
            LocalDate rewardDate,
            Instant createdAt) {

        if (coinsEarned <= 0) {
            throw new IllegalArgumentException(
                    "Coins earned must be positive.");
        }

        this.transactionId = Objects.requireNonNull(transactionId);
        this.userId = Objects.requireNonNull(userId);
        this.coinsEarned = coinsEarned;
        this.rewardDate = Objects.requireNonNull(rewardDate);
        this.createdAt = Objects.requireNonNull(createdAt);
    }

    public String getTransactionId() {
        return transactionId;
    }

    public String getUserId() {
        return userId;
    }

    public int getCoinsEarned() {
        return coinsEarned;
    }

    public LocalDate getRewardDate() {
        return rewardDate;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }
}