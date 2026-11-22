package entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

/**
 * One canonical transaction entity for both dashboard records and account-linked transactions.
 * Persistence, currency-rate lookup, and UI concerns are handled outside this entity.
 */
public class Transaction {
    private final int id;
    private final TransactionType type;
    private final BigDecimal amount;
    private final String description;
    private final LocalDate date;
    private final Account account;
    private final String transactionCurrency;
    private final Category category;
    private final String note;

    /** Constructor used by the existing income/expense dashboard data. */
    public Transaction(int id, TransactionType type, BigDecimal amount,
                       String description, LocalDate date) {
        this(id, type, amount, description, date, null, null, null, null);
    }

    /** Constructor for transactions associated with a financial account. */
    public Transaction(int id, TransactionType type, BigDecimal amount,
                       String description, LocalDate date, Account account,
                       String transactionCurrency, Category category, String note) {
        if (id < 0) throw new IllegalArgumentException("Transaction ID cannot be negative.");
        if (type == null) throw new IllegalArgumentException("Transaction type is required.");
        if (amount == null || amount.signum() <= 0) {
            throw new IllegalArgumentException("Amount must be positive.");
        }
        if (description == null || description.isBlank()) {
            throw new IllegalArgumentException("Description cannot be empty.");
        }
        this.id = id;
        this.type = type;
        this.amount = amount;
        this.description = description.trim();
        this.date = Objects.requireNonNull(date, "Date is required.");
        this.account = account;
        this.transactionCurrency = transactionCurrency;
        this.category = category;
        this.note = note;
        if (account != null && (transactionCurrency == null || transactionCurrency.isBlank())) {
            throw new IllegalArgumentException("Currency is required for account transactions.");
        }
    }

    public int getId() { return id; }
    public int getTransactionID() { return id; }
    public TransactionType getType() { return type; }
    public BigDecimal getAmount() { return amount; }
    public LocalDate getDate() { return date; }
    public String getDescription() { return description; }
    public Account getAccount() { return account; }
    public String getCurrency() { return transactionCurrency; }
    public Category getCategory() { return category; }
    public String getNote() { return note; }

    /** Applies an already-converted amount to the linked account balance. */
    public void applyToAccount(BigDecimal amountInAccountCurrency) {
        if (account == null) throw new IllegalStateException("This transaction is not linked to an account.");
        if (type == TransactionType.INCOME) {
            account.deposit(amountInAccountCurrency);
        } else {
            account.withdraw(amountInAccountCurrency);
        }
    }
}
