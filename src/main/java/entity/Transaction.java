package entity;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Locale;
import java.util.Objects;

/**
 * An immutable record of a portfolio trade.
 */
public final class Transaction {
    public enum Type {
        BUY,
        SELL
    }

    private final String ticker;
    private final int quantity;
    private final BigDecimal price;
    private final Type type;
    private final Instant timestamp;

    public Transaction(String ticker, int quantity, BigDecimal price, Type type) {
        this(ticker, quantity, price, type, Instant.now());
    }

    public Transaction(String ticker, int quantity, BigDecimal price, Type type,
                       Instant timestamp) {
        this.ticker = normalizeTicker(ticker);
        if (quantity <= 0) {
            throw new IllegalArgumentException("quantity must be positive");
        }
        Objects.requireNonNull(price, "price must not be null");
        if (price.signum() <= 0) {
            throw new IllegalArgumentException("price must be positive");
        }
        this.quantity = quantity;
        this.price = price;
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

    public Type getType() {
        return type;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    private static String normalizeTicker(String ticker) {
        Objects.requireNonNull(ticker, "ticker must not be null");
        String normalized = ticker.trim().toUpperCase(Locale.ROOT);
        if (normalized.isEmpty()) {
            throw new IllegalArgumentException("ticker must not be blank");
        }
        return normalized;
    }
}
