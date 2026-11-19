
import java.time.LocalDate;

public abstract class Transaction {

    protected final int transactionID;
    protected double amount;
    protected LocalDate date;
    protected String description;
    protected Account account;

    public Transaction(int transactionID, double amount,
                       LocalDate date, String description,
                       Account account) {

        if (amount <= 0) {
            throw new IllegalArgumentException(
                    "Amount must be positive"
            );
        }

        this.transactionID = transactionID;
        this.amount = amount;
        this.date = date;
        this.description = description;
        this.account = account;
    }

    // Getters
    public int getTransactionID() {
        return transactionID;
    }

    public double getAmount() {
        return amount;
    }

    public LocalDate getDate() {
        return date;
    }

    public String getDescription() {
        return description;
    }

    public Account getAccount() {
        return account;
    }

    // Abstract method
    public abstract void execute();
}
