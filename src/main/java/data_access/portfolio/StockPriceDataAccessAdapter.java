package data_access.portfolio;

import stockprototype.StockDataSource;
import use_case.portfolio.StockPriceDataAccessInterface;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;
import java.util.Objects;

/**
 * Adapts daily closing prices from the stock prototype to the portfolio use case.
 */
public final class StockPriceDataAccessAdapter implements StockPriceDataAccessInterface {
    private final StockDataSource stockDataSource;

    public StockPriceDataAccessAdapter(StockDataSource stockDataSource) {
        this.stockDataSource = Objects.requireNonNull(
                stockDataSource, "stockDataSource must not be null");
    }

    @Override
    public BigDecimal getCurrentPrice(String ticker) throws IOException {
        Map.Entry<LocalDate, BigDecimal> latestPrice = stockDataSource
                .getDailyClosingPrices(ticker)
                .lastEntry();
        if (latestPrice == null) {
            throw new IOException("No daily closing price was returned for " + ticker + ".");
        }
        return latestPrice.getValue();
    }
}
