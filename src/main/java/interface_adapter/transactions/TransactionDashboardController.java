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
    public void loadUsers() { inputBoundary.execute(TransactionDashboardInputData.loadUsers()); }
    public void loadUser(String name) { inputBoundary.execute(TransactionDashboardInputData.loadUser(name)); }
    public void addTransaction(String name, TransactionType type, BigDecimal amount,
                               String description, LocalDate date) {
        inputBoundary.execute(TransactionDashboardInputData.add(name, type, amount, description, date));
    }
    public void deleteTransaction(String name, TransactionType type, int id) {
        inputBoundary.execute(TransactionDashboardInputData.delete(name, type, id));
    }
}
