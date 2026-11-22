package entity;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Domain base for account-linked transactions that carry category and currency details.
 * It is separate from the lightweight dashboard Transaction record.
 */
public abstract class AccountTransaction {
    protected final BigDecimal amount;
    protected final LocalDate date;
    protected final String description;
    protected final Account account;
    protected final String transactionCurrency;
    protected Category category;
    protected String note;

    protected AccountTransaction(BigDecimal amount, LocalDate date, String description,
            Account account, String transactionCurrency, Category category, String note) {
        if (amount == null || amount.signum() <= 0) {
            throw new IllegalArgumentException("Amount must be positive.");
        }
        if (date == null) throw new IllegalArgumentException("Date is required.");
        if (description == null || description.isBlank()) {
            throw new IllegalArgumentException("Description cannot be empty.");
        }
        if (account == null) throw new IllegalArgumentException("Account is required.");
        if (transactionCurrency == null || transactionCurrency.isBlank()) {
            throw new IllegalArgumentException("Currency is required.");
        }
        this.amount = amount;
        this.date = date;
        this.description = description.trim();
        this.account = account;
        this.transactionCurrency = transactionCurrency;
        this.category = category;
        this.note = note;
    }

    public BigDecimal getAmount() { return amount; }
    public LocalDate getDate() { return date; }
    public String getDescription() { return description; }
    public Account getAccount() { return account; }
    public String getCurrency() { return transactionCurrency; }
    public Category getCategory() { return category; }
    public String getNote() { return note; }

    public abstract void applyToAccount();
}
