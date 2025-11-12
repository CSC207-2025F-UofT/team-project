package entity;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a position in a particular stock within a portfolio.
 */
public class Position {
    private final String ticker;
    private int quantity;
    private double averageCost;
    private final List<Trade> trades;

    public Position(String ticker) {
        this.ticker = ticker;
        this.quantity = 0;
        this.averageCost = 0.0;
        this.trades = new ArrayList<>();
    }

    public String getTicker() {
        return ticker;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getAverageCost() {
        return averageCost;
    }

    public List<Trade> getTrades() {
        return new ArrayList<>(trades);
    }

    /**
     * Add a trade to this position and update quantity and average cost.
     * TODO: Implement proper average cost calculation based on buy/sell trades
     */
    public void addTrade(Trade trade) {
        trades.add(trade);
        
        if (trade.isBuy()) {
            // TODO: Calculate new average cost when buying
            double totalCost = (averageCost * quantity) + (trade.getPrice() * trade.getQuantity());
            quantity += trade.getQuantity();
            averageCost = quantity > 0 ? totalCost / quantity : 0;
        } else {
            // Selling
            quantity -= trade.getQuantity();
            // TODO: Handle realized gains calculation
        }
    }

    /**
     * Calculate current market value of this position.
     */
    public double currentMarketValue(double currentPrice) {
        return quantity * currentPrice;
    }

    /**
     * Calculate unrealized gain/loss for this position.
     * TODO: Verify calculation logic
     */
    public double unrealizedGain(double currentPrice) {
        return (currentPrice - averageCost) * quantity;
    }

    /**
     * TODO: Implement realized gains calculation from closed trades
     */
    public double getRealizedGains() {
        // Calculate gains from sell trades
        return 0.0; // Placeholder
    }
}
