package use_case.watchlist;

import java.io.IOException;
import java.util.List;

public interface WatchlistStockDataAccessInterface {

    /**
     * Returns the last 'count' daily closing prices, OLDEST FIRST (latest is last).
     * Returns an empty list if count is zero or negative.
     * Throws IOException if the prices cannot be retrieved.
     */
    List<Double> getStockPrice(String stockWatcher, int count) throws IOException;
}
