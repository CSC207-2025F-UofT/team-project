package entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

/** A dashboard transaction record. Persistence is handled by a Data Access implementation. */
public final class Transaction {
    private final int id;
    private final TransactionType type;
    private final BigDecimal amount;
    private final String description;
    private final LocalDate date;

    public Transaction(int id, TransactionType type, BigDecimal amount,
                       String description, LocalDate date) {
        if (id < 0) throw new IllegalArgumentException("Transaction ID cannot be negative.");
        if (type == null) throw new IllegalArgumentException("Transaction type is required.");
        if (amount == null || amount.signum() <= 0) throw new IllegalArgumentException("Amount must be positive.");
        if (description == null || description.isBlank()) throw new IllegalArgumentException("Description cannot be empty.");
        this.id = id;
        this.type = type;
        this.amount = amount;
        this.description = description.trim();
        this.date = Objects.requireNonNull(date, "Date is required.");
    }

    public int getId() { return id; }
    public TransactionType getType() { return type; }
    public BigDecimal getAmount() { return amount; }
    public String getDescription() { return description; }
    public LocalDate getDate() { return date; }
}
