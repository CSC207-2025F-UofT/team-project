package interface_adapter.transactions;

import use_case.transactions.TransactionDashboardInputBoundary;
import use_case.transactions.TransactionDashboardInputData;
import entity.TransactionType;
import java.math.BigDecimal;
import java.time.LocalDate;

public final class TransactionDashboardController {
    private final TransactionDashboardInputBoundary inputBoundary;
    public TransactionDashboardController(TransactionDashboardInputBoundary inputBoundary) {
        this.inputBoundary = inputBoundary;
    }
    public void loadAccounts() { inputBoundary.execute(TransactionDashboardInputData.loadAccounts()); }
    public void loadAccount(int accountId) { inputBoundary.execute(TransactionDashboardInputData.loadAccount(accountId)); }
    public void addTransaction(int accountId, TransactionType type, BigDecimal amount,
                               String description, LocalDate date) {
        inputBoundary.execute(TransactionDashboardInputData.add(accountId, type, amount, description, date));
    }
    public void deleteTransaction(int accountId, TransactionType type, int id) {
        inputBoundary.execute(TransactionDashboardInputData.delete(accountId, type, id));
    }
}
