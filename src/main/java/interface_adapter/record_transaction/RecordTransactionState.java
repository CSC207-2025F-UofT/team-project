package interface_adapter.record_transaction;

import entity.Transaction;

/**
 * The state for the Record Transaction View Model.
 */
public class RecordTransactionState {

    private String amount = "";
    private Transaction.Type type = Transaction.Type.EXPENSE;
    private String error;
    private String successMessage;

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public Transaction.Type getType() {
        return type;
    }

    public void setType(Transaction.Type type) {
        this.type = type;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getSuccessMessage() {
        return successMessage;
    }

    public void setSuccessMessage(String successMessage) {
        this.successMessage = successMessage;
    }
}