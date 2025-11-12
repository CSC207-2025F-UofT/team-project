package data_access;

import java.util.Map;

/**
 * Interface for providing stock price data.
 * Used by domain entities to calculate unrealized gains.
 */
public interface PriceProvider {
    /**
     * Get the latest prices for multiple stock tickers.
     * @param tickers Array of stock ticker symbols
     * @return Map of ticker to latest price
     */
    Map<String, Double> getLatestPrices(String[] tickers);
}
