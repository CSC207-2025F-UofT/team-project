
import java.time.LocalDateTime;

public class Account {

    // Attributes
    private final int accountID;
    private String accountName;
    private AccountType accountType;
    private double balance;
    private String currency;
    private final LocalDateTime createdAt;

    // Constructor
    public Account(int accountID, String accountName,
                   AccountType accountType,
                   double balance, String currency) {

        if (accountName == null || accountName.isBlank()) {
            throw new IllegalArgumentException(
                    "Account name cannot be empty"
            );
        }

        this.accountID = accountID;
        this.accountName = accountName;
        this.accountType = accountType;
        this.balance = balance;
        this.currency = currency;
        this.createdAt = LocalDateTime.now();
    }

    // Getters
    public int getAccountID() {
        return accountID;
    }

    public String getAccountName() {
        return accountName;
    }

    public AccountType getAccountType() {
        return accountType;
    }

    public double getBalance() {
        return balance;
    }

    public String getCurrency() {
        return currency;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    // Setters
    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public void setAccountType(AccountType accountType) {
        this.accountType = accountType;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    // Balance operations
    public void deposit(double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException(
                    "Amount must be positive"
            );
        }
        balance += amount;
    }

    public void withdraw(double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException(
                    "Amount must be positive"
            );
        }
        balance -= amount;
    }
}
