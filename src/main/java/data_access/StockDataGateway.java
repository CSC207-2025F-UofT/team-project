package data_access;

import java.time.LocalDate;
import java.util.Map;

import entity.PricePoint;

/**
 * Gateway interface for accessing external stock market data.
 */
public interface StockDataGateway extends PriceProvider {
    /**
     * Get the latest prices for multiple stock tickers.
     * @param tickers Array of stock ticker symbols
     * @return Map of ticker to latest price
     */
    @Override
    Map<String, Double> getLatestPrices(String[] tickers);

    /**
     * Get historical price data for a stock.
     * @param ticker Stock ticker symbol
     * @param start Start date
     * @param end End date
     * @return Array of price points
     */
    PricePoint[] getHistoricalPrices(String ticker, LocalDate start, LocalDate end);

    // TODO: Add method to get real-time quote data
    // TODO: Add method to validate ticker symbols
}
