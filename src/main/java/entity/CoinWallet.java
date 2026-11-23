package entity;

import java.util.Objects;

public class CoinWallet {
    private final String userId;
    private int balance;

    public CoinWallet(String userId, int balance) {
        this.userId = Objects.requireNonNull(userId);

        if (balance < 0) {
            throw new IllegalArgumentException(
                    "Balance cannot be negative.");
        }
        this.balance = balance;
    }

    public void addCoins(int amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException(
                    "Coins added must be positive.");
        }
        balance = Math.addExact(balance, amount);
    }

    public String getUserId() {
        return userId;
    }

    public int getBalance() {
        return balance;
    }
}