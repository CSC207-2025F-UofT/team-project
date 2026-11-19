package entity;

import interface_adapter.CurrencyControl;
import interface_adapter.TransactionControl;

import java.io.IOException;
import java.time.LocalDate;

public abstract class Transaction {

    protected int transactionID;
    protected double amount;
    protected LocalDate date;
    protected String description;
    protected Account account;
    protected String transactionCurrency;
    protected Category category;
    protected String note;

    protected Transaction(double amount,
                          LocalDate date, String description,
                          Account account, String transactionCurrency, Category category, String note, String source) throws IOException {
      String currencyDatabase = "Currencies.json";
      CurrencyControl allCurrencies = new CurrencyControl(currencyDatabase);
        if (amount <= 0) {
          throw new IllegalArgumentException(
              "Amount must be positive"
          );
        } else if (!allCurrencies.validCurrency(transactionCurrency)) {
            throw new IllegalArgumentException("The currency type is not supported. Please try again with another currency.");
        } else {
          TransactionControl transaction = new TransactionControl(source);
            this.transactionID = transaction.writeTransactions(account, amount, date, description, category, note, transactionCurrency);
            this.amount = amount;
            this.date = date;
            this.note = note;
            this.description = description;
            this.category = category;
            this.account = account;
            this.transactionCurrency = transactionCurrency;
    }}

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
