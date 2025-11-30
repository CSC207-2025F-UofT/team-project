package entity;

import java.time.LocalDate;

public class Transaction {

    public enum TransactionType {
        EXPENSE,
        INCOME
    }

    public enum TransactionCategory {
        SALARY,
        DINING,
        GROCERIES,
        RENT,
        UTILITIES,
        ENTERTAINMENT,
        TRANSPORTATION,
        MORTGAGE,
        HEALTHCARE
    }

    private final double amount;
    private final TransactionType type;
    private final TransactionCategory category;
    private final LocalDate date;
    private final String accountNumber;

    public Transaction(double amount, TransactionType type, TransactionCategory category, LocalDate date, String accountNumber) {
        this.amount = amount;
        this.type = type;
        this.category = category;
        this.date = date;
        this.accountNumber = accountNumber;
    }

    public double getTransactionAmount() {
        return this.amount;
    }

    public TransactionType getTransactionType() {
        return this.type;
    }

    public TransactionCategory getTransactionCategory() {
        return this.category;
    }

    public LocalDate getTransactionDate() {
        return this.date;
    }

    public String getAccountNumber() {
        return this.accountNumber;
    }
}
