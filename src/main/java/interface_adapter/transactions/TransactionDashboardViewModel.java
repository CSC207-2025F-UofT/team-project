package interface_adapter.transactions;

import entity.Account;
import entity.Transaction;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/** Display state shared with the Swing dashboard. */
public final class TransactionDashboardViewModel {
    private List<Account> accounts = new ArrayList<>();
    private List<Transaction> income = new ArrayList<>();
    private List<Transaction> expenses = new ArrayList<>();
    private BigDecimal totalIncome = BigDecimal.ZERO;
    private BigDecimal totalExpenses = BigDecimal.ZERO;
    private String message = "";
    private boolean error;

    public List<Account> getAccounts() { return accounts; }
    public List<Transaction> getIncome() { return income; }
    public List<Transaction> getExpenses() { return expenses; }
    public BigDecimal getTotalIncome() { return totalIncome; }
    public BigDecimal getTotalExpenses() { return totalExpenses; }
    public BigDecimal getBalance() { return totalIncome.subtract(totalExpenses); }
    public String getMessage() { return message; }
    public boolean isError() { return error; }

    void setAccounts(List<Account> value) { accounts = new ArrayList<>(value); }
    void setIncome(List<Transaction> value) { income = new ArrayList<>(value); }
    void setExpenses(List<Transaction> value) { expenses = new ArrayList<>(value); }
    void setTotals(BigDecimal incomeTotal, BigDecimal expenseTotal) {
        totalIncome = incomeTotal; totalExpenses = expenseTotal;
    }
    void setMessage(String value) { message = value; }
    void setError(boolean value) { error = value; }
}
