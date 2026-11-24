package entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneOffset;

public class Transaction {

    public enum Type {
        INCOME,
        EXPENSE
    }

    private final BigDecimal amount;
    private final Type type;
    private final LocalDate date;

    public Transaction(BigDecimal amount, Type type) {
        this(amount, type, LocalDate.now(ZoneOffset.UTC));
    }

    public Transaction(BigDecimal amount, Type type, LocalDate date) {
        if (amount == null || amount.signum() <= 0) {
            throw new IllegalArgumentException("Amount must be positive.");
        }
        if (type == null) {
            throw new IllegalArgumentException("Transaction type is required.");
        }
        if (date == null) {
            throw new IllegalArgumentException("Transaction date is required.");
        }

        this.amount = amount;
        this.type = type;
        this.date = date;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public Type getType() {
        return type;
    }

    public LocalDate getDate() {
        return date;
    }
}