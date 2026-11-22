package entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

/** A financial account owned by one UserProfile. No persistence or UI dependencies. */
public final class Account {
    private final int accountID;
    private final int ownerProfileId;
    private String accountName;
    private AccountType accountType;
    private BigDecimal balance;
    private String currency;
    private final LocalDateTime createdAt;

    public Account(int accountID, int ownerProfileId, String accountName,
                   AccountType accountType, BigDecimal initialBalance, String currency) {
        if (accountID < 0 || ownerProfileId < 0) {
            throw new IllegalArgumentException("IDs cannot be negative.");
        }
        if (accountName == null || accountName.isBlank()) {
            throw new IllegalArgumentException("Account name cannot be empty.");
        }
        if (accountType == null) throw new IllegalArgumentException("Account type is required.");
        if (initialBalance == null || initialBalance.signum() < 0) {
            throw new IllegalArgumentException("Initial balance cannot be negative.");
        }
        if (currency == null || !currency.matches("[A-Z]{3}")) {
            throw new IllegalArgumentException("Currency must be a three-letter ISO code.");
        }
        this.accountID = accountID;
        this.ownerProfileId = ownerProfileId;
        this.accountName = accountName.trim();
        this.accountType = accountType;
        this.balance = initialBalance;
        this.currency = currency;
        this.createdAt = LocalDateTime.now();
    }

    public int getAccountID() { return accountID; }
    public int getId() { return accountID; }
    public int getOwnerProfileId() { return ownerProfileId; }
    public String getAccountName() { return accountName; }
    public AccountType getAccountType() { return accountType; }
    public BigDecimal getBalance() { return balance; }
    public String getCurrency() { return currency; }
    public LocalDateTime getCreatedAt() { return createdAt; }

    public void rename(String newName) {
        if (newName == null || newName.isBlank()) {
            throw new IllegalArgumentException("Account name cannot be empty.");
        }
        accountName = newName.trim();
    }

    public void changeType(AccountType newType) {
        accountType = Objects.requireNonNull(newType, "Account type is required.");
    }

    public void deposit(BigDecimal amount) {
        requirePositive(amount);
        balance = balance.add(amount);
    }

    public void withdraw(BigDecimal amount) {
        requirePositive(amount);
        if (balance.compareTo(amount) < 0) {
            throw new IllegalArgumentException("Insufficient funds.");
        }
        balance = balance.subtract(amount);
    }

    /** Applies a rate expressed as target-currency units per source-currency unit. */
    public void changeCurrency(String targetCurrency, BigDecimal exchangeRate) {
        if (targetCurrency == null || !targetCurrency.matches("[A-Z]{3}")) {
            throw new IllegalArgumentException("Currency must be a three-letter ISO code.");
        }
        if (exchangeRate == null || exchangeRate.signum() <= 0) {
            throw new IllegalArgumentException("Exchange rate must be positive.");
        }
        balance = balance.multiply(exchangeRate).setScale(2, java.math.RoundingMode.HALF_UP);
        currency = targetCurrency;
    }

    private void requirePositive(BigDecimal amount) {
        if (amount == null || amount.signum() <= 0) {
            throw new IllegalArgumentException("Amount must be positive.");
        }
    }
}
