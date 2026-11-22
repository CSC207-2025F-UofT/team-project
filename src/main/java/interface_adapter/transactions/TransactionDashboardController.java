package interface_adapter.transactions;

import application_business_rules.transactions.TransactionDashboardInputBoundary;
import application_business_rules.transactions.TransactionDashboardInputData;
import entity.Transaction;
import entity.TransactionType;

public final class TransactionDashboardController {
    private final TransactionDashboardInputBoundary inputBoundary;
    public TransactionDashboardController(TransactionDashboardInputBoundary inputBoundary) {
        this.inputBoundary = inputBoundary;
    }
    public void loadUsers() { inputBoundary.execute(TransactionDashboardInputData.loadUsers()); }
    public void loadUser(String name) { inputBoundary.execute(TransactionDashboardInputData.loadUser(name)); }
    public void addTransaction(String name, Transaction transaction) {
        inputBoundary.execute(TransactionDashboardInputData.add(name, transaction));
    }
    public void deleteTransaction(String name, TransactionType type, int id) {
        inputBoundary.execute(TransactionDashboardInputData.delete(name, type, id));
    }
}
