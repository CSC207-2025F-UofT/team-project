package application_business_rules.transactions;

import entity.Transaction;
import entity.TransactionType;

public final class TransactionDashboardInputData {
    public enum Action { LOAD_USERS, LOAD_USER, ADD_TRANSACTION, DELETE_TRANSACTION }
    private final Action action;
    private final String userName;
    private final Transaction transaction;
    private final TransactionType type;
    private final int transactionId;

    private TransactionDashboardInputData(Action action, String userName,
            Transaction transaction, TransactionType type, int transactionId) {
        this.action = action; this.userName = userName; this.transaction = transaction;
        this.type = type; this.transactionId = transactionId;
    }
    public static TransactionDashboardInputData loadUsers() {
        return new TransactionDashboardInputData(Action.LOAD_USERS, null, null, null, -1);
    }
    public static TransactionDashboardInputData loadUser(String userName) {
        return new TransactionDashboardInputData(Action.LOAD_USER, userName, null, null, -1);
    }
    public static TransactionDashboardInputData add(String userName, Transaction transaction) {
        return new TransactionDashboardInputData(Action.ADD_TRANSACTION, userName, transaction, null, -1);
    }
    public static TransactionDashboardInputData delete(String userName, TransactionType type, int id) {
        return new TransactionDashboardInputData(Action.DELETE_TRANSACTION, userName, null, type, id);
    }
    public Action getAction() { return action; }
    public String getUserName() { return userName; }
    public Transaction getTransaction() { return transaction; }
    public TransactionType getType() { return type; }
    public int getTransactionId() { return transactionId; }
}
