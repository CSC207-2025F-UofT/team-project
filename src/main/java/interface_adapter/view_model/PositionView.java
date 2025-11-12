package interface_adapter.view_model;

/**
 * View model representing a single position for display.
 */
public class PositionView {
    private final String ticker;
    private final int quantity;
    private final double marketPrice;
    private final double averageCost;
    private final double unrealizedGain;

    public PositionView(String ticker, int quantity, double marketPrice, 
                       double averageCost, double unrealizedGain) {
        this.ticker = ticker;
        this.quantity = quantity;
        this.marketPrice = marketPrice;
        this.averageCost = averageCost;
        this.unrealizedGain = unrealizedGain;
    }

    public String getTicker() {
        return ticker;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getMarketPrice() {
        return marketPrice;
    }

    public double getAverageCost() {
        return averageCost;
    }

    public double getUnrealizedGain() {
        return unrealizedGain;
    }

    // TODO: Add formatted string methods for currency display
    public String getFormattedGain() {
        return String.format("$%.2f", unrealizedGain);
    }

    public String getFormattedMarketValue() {
        return String.format("$%.2f", marketPrice * quantity);
    }
}
