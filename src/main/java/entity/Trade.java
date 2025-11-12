package entity;

import java.time.LocalDateTime;

/**
 * Represents a single trade (buy or sell) of a stock.
 */
public class Trade {
    private final String tradeId;
    private final String ticker;
    private final int quantity;
    private final double price;
    private final LocalDateTime timestamp;
    private final boolean isBuy;

    public Trade(String tradeId, String ticker, int quantity, double price, 
                 LocalDateTime timestamp, boolean isBuy) {
        this.tradeId = tradeId;
        this.ticker = ticker;
        this.quantity = quantity;
        this.price = price;
        this.timestamp = timestamp;
        this.isBuy = isBuy;
    }

    public String getTradeId() {
        return tradeId;
    }

    public String getTicker() {
        return ticker;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getPrice() {
        return price;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public boolean isBuy() {
        return isBuy;
    }

    // TODO: Add method to calculate trade value
    public double getTradeValue() {
        return quantity * price;
    }
}
