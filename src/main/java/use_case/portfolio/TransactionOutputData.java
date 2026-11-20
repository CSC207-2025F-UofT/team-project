package use_case.portfolio;

import entity.Transaction;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Objects;

/**
 * Presentation data for one portfolio transaction.
 */
public final class TransactionOutputData {
    private final String ticker;
    private final int quantity;
    private final BigDecimal price;
    private final Transaction.Type type;
    private final Instant timestamp;

    public TransactionOutputData(String ticker, int quantity, BigDecimal price,
                                 Transaction.Type type, Instant timestamp) {
        this.ticker = requireNonBlank(ticker, "ticker");
        if (quantity <= 0) {
            throw new IllegalArgumentException("quantity must be positive");
        }
        this.quantity = quantity;
        this.price = requirePositive(price, "price");
        this.type = Objects.requireNonNull(type, "type must not be null");
        this.timestamp = Objects.requireNonNull(timestamp, "timestamp must not be null");
    }

    public String getTicker() {
        return ticker;
    }

    public int getQuantity() {
        return quantity;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public Transaction.Type getType() {
        return type;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    private static String requireNonBlank(String value, String fieldName) {
        Objects.requireNonNull(value, fieldName + " must not be null");
        String trimmed = value.trim();
        if (trimmed.isEmpty()) {
            throw new IllegalArgumentException(fieldName + " must not be blank");
        }
        return trimmed;
    }

    private static BigDecimal requirePositive(BigDecimal value, String fieldName) {
        Objects.requireNonNull(value, fieldName + " must not be null");
        if (value.signum() <= 0) {
            throw new IllegalArgumentException(fieldName + " must be positive");
        }
        return value;
    }
}
