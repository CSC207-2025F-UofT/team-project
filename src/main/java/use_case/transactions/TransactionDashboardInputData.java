package use_case.transactions;

import entity.TransactionType;
import java.math.BigDecimal;
import java.time.LocalDate;

public final class TransactionDashboardInputData {
    public enum Action { LOAD_USERS, LOAD_USER, ADD_TRANSACTION, DELETE_TRANSACTION }
    private final Action action;
    private final String userName;
    private final TransactionType type;
    private final BigDecimal amount;
    private final String description;
    private final LocalDate date;
    private final int transactionId;

    private TransactionDashboardInputData(Action action, String userName, TransactionType type,
            BigDecimal amount, String description, LocalDate date, int transactionId) {
        this.action = action; this.userName = userName; this.type = type;
        this.amount = amount; this.description = description; this.date = date;
        this.transactionId = transactionId;
    }
    public static TransactionDashboardInputData loadUsers() {
        return new TransactionDashboardInputData(Action.LOAD_USERS, null, null, null, null, null, -1);
    }
    public static TransactionDashboardInputData loadUser(String userName) {
        return new TransactionDashboardInputData(Action.LOAD_USER, userName, null, null, null, null, -1);
    }
    public static TransactionDashboardInputData add(String userName, TransactionType type,
            BigDecimal amount, String description, LocalDate date) {
        return new TransactionDashboardInputData(Action.ADD_TRANSACTION, userName, type,
            amount, description, date, -1);
    }
    public static TransactionDashboardInputData delete(String userName, TransactionType type, int id) {
        return new TransactionDashboardInputData(Action.DELETE_TRANSACTION, userName, type,
            null, null, null, id);
    }
    public Action getAction() { return action; }
    public String getUserName() { return userName; }
    public TransactionType getType() { return type; }
    public BigDecimal getAmount() { return amount; }
    public String getDescription() { return description; }
    public LocalDate getDate() { return date; }
    public int getTransactionId() { return transactionId; }
}
