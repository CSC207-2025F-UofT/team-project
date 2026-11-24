package use_case.transactions;

import entity.TransactionType;
import java.math.BigDecimal;
import java.time.LocalDate;

public final class TransactionDashboardInputData {
    public enum Action { LOAD_ACCOUNTS, LOAD_ACCOUNT, ADD_TRANSACTION, DELETE_TRANSACTION }
    private final Action action;
    private final int accountId;
    private final TransactionType type;
    private final BigDecimal amount;
    private final String description;
    private final LocalDate date;
    private final int transactionId;

    private TransactionDashboardInputData(Action action, int accountId, TransactionType type,
            BigDecimal amount, String description, LocalDate date, int transactionId) {
        this.action = action; this.accountId = accountId; this.type = type;
        this.amount = amount; this.description = description; this.date = date;
        this.transactionId = transactionId;
    }
    public static TransactionDashboardInputData loadAccounts() {
        return new TransactionDashboardInputData(Action.LOAD_ACCOUNTS, -1, null, null, null, null, -1);
    }
    public static TransactionDashboardInputData loadAccount(int accountId) {
        return new TransactionDashboardInputData(Action.LOAD_ACCOUNT, accountId, null, null, null, null, -1);
    }
    public static TransactionDashboardInputData add(int accountId, TransactionType type,
            BigDecimal amount, String description, LocalDate date) {
        return new TransactionDashboardInputData(Action.ADD_TRANSACTION, accountId, type, amount, description, date, -1);
    }
    public static TransactionDashboardInputData delete(int accountId, TransactionType type, int id) {
        return new TransactionDashboardInputData(Action.DELETE_TRANSACTION, accountId, type, null, null, null, id);
    }
    public Action getAction() { return action; }
    public int getAccountId() { return accountId; }
    public TransactionType getType() { return type; }
    public BigDecimal getAmount() { return amount; }
    public String getDescription() { return description; }
    public LocalDate getDate() { return date; }
    public int getTransactionId() { return transactionId; }
}
