package entity;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/** Chequing account: allows withdrawals up to a configured overdraft limit. */
public final class ChequingAccount extends Account {
    private BigDecimal overdraftLimit;
    public ChequingAccount(int id, int ownerId, String name, BigDecimal balance, String currency) {
        this(id, ownerId, name, balance, currency, BigDecimal.ZERO, LocalDateTime.now());
    }
    public ChequingAccount(int id, int ownerId, String name, BigDecimal balance, String currency, BigDecimal limit, LocalDateTime createdAt) {
        super(id, ownerId, name, balance, currency, createdAt);
        setOverdraftLimit(limit);
        if (getBalance().compareTo(overdraftLimit.negate()) < 0)
            throw new IllegalArgumentException("Initial balance exceeds the overdraft limit.");
    }
    @Override public AccountType getAccountType() { return AccountType.CHEQUING; }
    @Override protected boolean canWithdraw(BigDecimal amount) { return getBalance().subtract(amount).compareTo(overdraftLimit.negate()) >= 0; }
    public BigDecimal getOverdraftLimit() { return overdraftLimit; }
    public void setOverdraftLimit(BigDecimal limit) {
        if (limit == null || limit.signum() < 0) throw new IllegalArgumentException("Overdraft limit cannot be negative.");
        overdraftLimit = limit;
    }
}
