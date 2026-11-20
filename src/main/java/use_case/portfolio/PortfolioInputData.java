package use_case.portfolio;

import java.util.Locale;
import java.util.Objects;

/**
 * Request data for a portfolio operation.
 */
public final class PortfolioInputData {
    public enum Operation {
        BUY,
        SELL,
        VIEW,
        TRANSACTION_HISTORY
    }

    private final String username;
    private final String ticker;
    private final int quantity;
    private final Operation operation;

    public PortfolioInputData(String username, Operation operation) {
        this(username, null, 0, operation);
    }

    public PortfolioInputData(String username, String ticker, int quantity,
                              Operation operation) {
        this.username = requireNonBlank(username, "username");
        this.operation = Objects.requireNonNull(operation, "operation must not be null");

        if (operation == Operation.BUY || operation == Operation.SELL) {
            this.ticker = normalizeTicker(ticker);
            if (quantity <= 0) {
                throw new IllegalArgumentException("quantity must be positive");
            }
            this.quantity = quantity;
        } else {
            if (ticker != null || quantity != 0) {
                throw new IllegalArgumentException(
                        "ticker and quantity are only valid for buy and sell operations");
            }
            this.ticker = null;
            this.quantity = 0;
        }
    }

    public String getUsername() {
        return username;
    }

    public String getTicker() {
        return ticker;
    }

    public int getQuantity() {
        return quantity;
    }

    public Operation getOperation() {
        return operation;
    }

    private static String normalizeTicker(String ticker) {
        return requireNonBlank(ticker, "ticker").toUpperCase(Locale.ROOT);
    }

    private static String requireNonBlank(String value, String fieldName) {
        Objects.requireNonNull(value, fieldName + " must not be null");
        String trimmed = value.trim();
        if (trimmed.isEmpty()) {
            throw new IllegalArgumentException(fieldName + " must not be blank");
        }
        return trimmed;
    }
}
