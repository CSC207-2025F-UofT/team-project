package entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import data_access.PriceProvider;

/**
 * Represents a user's portfolio containing multiple positions and cash.
 */
public class Portfolio {
    private final String id;
    private final String ownerId;
    private final List<Position> positions;
    private double cash;

    public Portfolio(String id, String ownerId, double initialCash) {
        this.id = id;
        this.ownerId = ownerId;
        this.positions = new ArrayList<>();
        this.cash = initialCash;
    }

    public String getId() {
        return id;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public List<Position> getPositions() {
        return new ArrayList<>(positions);
    }

    public double getCash() {
        return cash;
    }

    public void setCash(double cash) {
        this.cash = cash;
    }

    /**
     * Add a position to the portfolio.
     */
    public void addPosition(Position position) {
        positions.add(position);
    }

    /**
     * Find a position by ticker symbol.
     */
    public Position findPosition(String ticker) {
        return positions.stream()
                .filter(p -> p.getTicker().equals(ticker))
                .findFirst()
                .orElse(null);
    }

    /**
     * TODO: Implement realized gains calculation across all positions
     */
    public double calculateRealizedGains() {
        double totalRealizedGains = 0.0;
        for (Position position : positions) {
            totalRealizedGains += position.getRealizedGains();
        }
        return totalRealizedGains;
    }

    /**
     * Calculate unrealized gains using current market prices.
     * TODO: Implement proper error handling for missing prices
     */
    public double calculateUnrealizedGains(PriceProvider priceProvider) {
        double totalUnrealizedGains = 0.0;
        
        // Get all tickers
        String[] tickers = positions.stream()
                .map(Position::getTicker)
                .toArray(String[]::new);
        
        // Get current prices
        Map<String, Double> prices = priceProvider.getLatestPrices(tickers);
        
        // Calculate unrealized gains for each position
        for (Position position : positions) {
            Double currentPrice = prices.get(position.getTicker());
            if (currentPrice != null) {
                totalUnrealizedGains += position.unrealizedGain(currentPrice);
            }
            // TODO: Handle case when price is not available
        }
        
        return totalUnrealizedGains;
    }

    /**
     * TODO: Add method to calculate total portfolio value (positions + cash)
     */
    public double getTotalValue(PriceProvider priceProvider) {
        // Calculate total market value of all positions plus cash
        return 0.0; // Placeholder
    }
}
