package use_case.transactions;

import entity.Account;
import entity.Transaction;
import java.math.BigDecimal;
import java.util.List;

public final class TransactionDashboardOutputData {
    private final List<Account> accounts;
    private final List<Transaction> income;
    private final List<Transaction> expenses;
    private final BigDecimal totalIncome;
    private final BigDecimal totalExpenses;
    private final String message;

    public TransactionDashboardOutputData(List<Account> accounts, List<Transaction> income,
            List<Transaction> expenses, BigDecimal totalIncome, BigDecimal totalExpenses, String message) {
        this.accounts = accounts; this.income = income; this.expenses = expenses;
        this.totalIncome = totalIncome; this.totalExpenses = totalExpenses; this.message = message;
    }
    public List<Account> getAccounts() { return accounts; }
    public List<Transaction> getIncome() { return income; }
    public List<Transaction> getExpenses() { return expenses; }
    public BigDecimal getTotalIncome() { return totalIncome; }
    public BigDecimal getTotalExpenses() { return totalExpenses; }
    public BigDecimal getBalance() { return totalIncome.subtract(totalExpenses); }
    public String getMessage() { return message; }
}
