package entity;

import java.math.BigDecimal;

/**
 * An entity representing a user's wallet.
 */
public class Wallet {
    private final BigDecimal balance;

    /**
     * Creates a new wallet with the given balance.
     * @param balance the wallet's balance
     * @throws IllegalArgumentException if the balance is null
     */
    public Wallet(BigDecimal balance) {
        if (balance == null) {
            throw new IllegalArgumentException("Balance must not be null");
        }

        this.balance = balance;
    }

    public BigDecimal getBalance() {
        return balance;
    }
}
