package use_case.portfolio;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Presentation data for a user's transaction history.
 */
public final class TransactionHistoryOutputData {
    private final List<TransactionOutputData> transactions;

    public TransactionHistoryOutputData(List<TransactionOutputData> transactions) {
        Objects.requireNonNull(transactions, "transactions must not be null");
        if (transactions.stream().anyMatch(Objects::isNull)) {
            throw new NullPointerException("transactions must not contain null");
        }
        this.transactions = Collections.unmodifiableList(new ArrayList<>(transactions));
    }

    public List<TransactionOutputData> getTransactions() {
        return transactions;
    }
}
