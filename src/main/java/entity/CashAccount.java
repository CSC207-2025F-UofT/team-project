package entity;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/** Physical-cash account: tracks storage location and never permits overdrafts. */
public final class CashAccount extends Account {
    private String storageLocation;
    public CashAccount(int id, int ownerId, String name, BigDecimal balance, String currency) {
        this(id, ownerId, name, balance, currency, "Wallet", LocalDateTime.now());
    }
    public CashAccount(int id, int ownerId, String name, BigDecimal balance, String currency, String location, LocalDateTime createdAt) {
        super(id, ownerId, name, balance, currency, createdAt);
        if (balance.signum() < 0) throw new IllegalArgumentException("Cash balance cannot be negative.");
        setStorageLocation(location);
    }
    @Override public AccountType getAccountType() { return AccountType.CASH; }
    @Override protected boolean canWithdraw(BigDecimal amount) { return getBalance().compareTo(amount) >= 0; }
    public String getStorageLocation() { return storageLocation; }
    public void setStorageLocation(String location) {
        if (location == null || location.isBlank()) throw new IllegalArgumentException("Storage location cannot be empty.");
        storageLocation = location.trim();
    }
    public void depositCash(BigDecimal amount) { deposit(amount); }
    public void withdrawCash(BigDecimal amount) { withdraw(amount); }
}
