package use_case.transactions;

import entity.Transaction;
import java.math.BigDecimal;
import java.util.List;

public final class TransactionDashboardOutputData {
    private final List<String> users;
    private final List<Transaction> income;
    private final List<Transaction> expenses;
    private final BigDecimal totalIncome;
    private final BigDecimal totalExpenses;
    private final String message;

    public TransactionDashboardOutputData(List<String> users, List<Transaction> income,
            List<Transaction> expenses, BigDecimal totalIncome, BigDecimal totalExpenses, String message) {
        this.users = users; this.income = income; this.expenses = expenses;
        this.totalIncome = totalIncome; this.totalExpenses = totalExpenses; this.message = message;
    }
    public List<String> getUsers() { return users; }
    public List<Transaction> getIncome() { return income; }
    public List<Transaction> getExpenses() { return expenses; }
    public BigDecimal getTotalIncome() { return totalIncome; }
    public BigDecimal getTotalExpenses() { return totalExpenses; }
    public BigDecimal getBalance() { return totalIncome.subtract(totalExpenses); }
    public String getMessage() { return message; }
}
