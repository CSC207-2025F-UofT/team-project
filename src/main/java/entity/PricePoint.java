package entity;

import java.time.LocalDateTime;

/**
 * Represents a price data point at a specific time.
 */
public class PricePoint {
    private final LocalDateTime timestamp;
    private final double price;

    public PricePoint(LocalDateTime timestamp, double price) {
        this.timestamp = timestamp;
        this.price = price;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public double getPrice() {
        return price;
    }
}
