package entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

/** Shared state and behaviour for all financial account types. */
public abstract class Account {
    private final int accountID;
    private final int ownerProfileId;
    private String accountName;
    private BigDecimal balance;
    private String currency;
    private final LocalDateTime createdAt;

    protected Account(int id, int ownerId, String name, BigDecimal balance, String currency) {
        this(id, ownerId, name, balance, currency, LocalDateTime.now());
    }
    protected Account(int id, int ownerId, String name, BigDecimal balance, String currency, LocalDateTime createdAt) {
        if (id < 0 || ownerId < 0) throw new IllegalArgumentException("IDs cannot be negative.");
        if (name == null || name.isBlank()) throw new IllegalArgumentException("Account name cannot be empty.");
        if (balance == null || balance.signum() < 0) throw new IllegalArgumentException("Initial balance cannot be negative.");
        if (currency == null || !currency.matches("[A-Z]{3}")) throw new IllegalArgumentException("Currency must be a three-letter ISO code.");
        this.accountID = id; this.ownerProfileId = ownerId; this.accountName = name.trim();
        this.balance = balance; this.currency = currency; this.createdAt = Objects.requireNonNull(createdAt);
    }
    public abstract AccountType getAccountType();
    protected abstract boolean canWithdraw(BigDecimal amount);
    public int getAccountID() { return accountID; }
    public int getId() { return accountID; }
    public int getOwnerProfileId() { return ownerProfileId; }
    public String getAccountName() { return accountName; }
    public BigDecimal getBalance() { return balance; }
    public String getCurrency() { return currency; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void rename(String name) {
        if (name == null || name.isBlank()) throw new IllegalArgumentException("Account name cannot be empty.");
        accountName = name.trim();
    }
    public final void deposit(BigDecimal amount) { requirePositive(amount); balance = balance.add(amount); }
    public final void withdraw(BigDecimal amount) {
        requirePositive(amount);
        if (!canWithdraw(amount)) throw new IllegalArgumentException("Insufficient funds or withdrawal limit exceeded.");
        balance = balance.subtract(amount);
    }
    public final void changeCurrency(String targetCurrency, BigDecimal rate) {
        if (targetCurrency == null || !targetCurrency.matches("[A-Z]{3}")) throw new IllegalArgumentException("Currency must be a three-letter ISO code.");
        if (rate == null || rate.signum() <= 0) throw new IllegalArgumentException("Exchange rate must be positive.");
        balance = balance.multiply(rate).setScale(2, java.math.RoundingMode.HALF_UP);
        currency = targetCurrency;
    }
    protected final void requirePositive(BigDecimal amount) {
        if (amount == null || amount.signum() <= 0) throw new IllegalArgumentException("Amount must be positive.");
    }
    @Override public String toString() {
        return accountName + " (" + getAccountType().name().toLowerCase()
            + ", profile " + ownerProfileId + ", #" + accountID + ")";
    }
}
