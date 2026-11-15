
import java.io.IOException;
import java.time.LocalDate;

public abstract class Transaction {

    protected final int transactionID;
    protected double amount;
    protected LocalDate date;
    protected String description;
    protected Account account;
    protected String transactionCurrency;

    public Transaction(int transactionID, double amount,
                       LocalDate date, String description,
                       Account account, String transactionCurrency) throws IOException {
      String currencyDatabase = "Currencies.json";
      CurrencyControl allCurrencies = new CurrencyControl(currencyDatabase);
        if (amount <= 0) {
          throw new IllegalArgumentException(
              "Amount must be positive"
          );
        } else if (!allCurrencies.validCurrency(transactionCurrency)) {
            throw new IllegalArgumentException("The currency type is not supported. Please try again with another currency.");
        }

        this.transactionID = transactionID;
        this.amount = amount;
        this.date = date;
        this.description = description;
        this.account = account;
        this.transactionCurrency = transactionCurrency;
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

    public String getCurrency() {return transactionCurrency;}

    // Abstract method
    public abstract void execute() throws IOException;
}
