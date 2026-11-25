package entity;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;

/** Savings account: earns monthly interest and cannot be overdrawn. */
public final class SavingsAccount extends Account {
    private BigDecimal annualInterestRate;
    public SavingsAccount(int id, int ownerId, String name, BigDecimal balance, String currency) {
        this(id, ownerId, name, balance, currency, BigDecimal.ZERO, LocalDateTime.now());
    }
    public SavingsAccount(int id, int ownerId, String name, BigDecimal balance, String currency, BigDecimal rate, LocalDateTime createdAt) {
        super(id, ownerId, name, balance, currency, createdAt);
        if (balance.signum() < 0) throw new IllegalArgumentException("Savings balance cannot be negative.");
        setAnnualInterestRate(rate);
    }
    @Override public AccountType getAccountType() { return AccountType.SAVINGS; }
    @Override protected boolean canWithdraw(BigDecimal amount) { return getBalance().compareTo(amount) >= 0; }
    public BigDecimal getAnnualInterestRate() { return annualInterestRate; }
    public void setAnnualInterestRate(BigDecimal rate) {
        if (rate == null || rate.signum() < 0 || rate.compareTo(BigDecimal.ONE) > 0)
            throw new IllegalArgumentException("Annual interest rate must be between 0 and 1.");
        annualInterestRate = rate;
    }
    /** Applies one month's simple interest. A rate of 0.04 represents 4% annually. */
    public BigDecimal applyMonthlyInterest() {
        BigDecimal interest = getBalance().multiply(annualInterestRate).divide(new BigDecimal("12"), 2, RoundingMode.HALF_UP);
        if (interest.signum() > 0) deposit(interest);
        return interest;
    }
}
