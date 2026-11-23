package use_case.record_transaction;

import entity.Transaction;

/**
 * Input for recording a transaction.
 */
public class RecordTransactionInputData {

    private final String amount;
    private final Transaction.Type type;

    public RecordTransactionInputData(String amount, Transaction.Type type) {
        this.amount = amount;
        this.type = type;
    }

    public String getAmount() {
        return amount;
    }

    public Transaction.Type getType() {
        return type;
    }
}