package entity;

import java.math.BigDecimal;

/**
 * An income or expense transaction.
 */
public class Transaction {

    public enum Type {
        INCOME,
        EXPENSE
    }

    private final BigDecimal amount;
    private final Type type;

    public Transaction(BigDecimal amount, Type type) {
        if (amount == null || amount.signum() <= 0) {
            throw new IllegalArgumentException("Amount must be positive.");
        }
        if (type == null) {
            throw new IllegalArgumentException("Transaction type is required.");
        }

        this.amount = amount;
        this.type = type;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public Type getType() {
        return type;
    }
}