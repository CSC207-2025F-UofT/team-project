package use_case.watchlist;

import java.io.IOException;
import java.util.List;

public interface WatchlistStockDataAccessInterface {

    /** The last 'count' daily closing prices, OLDEST FIRST (latest is last). */
    List<Double> getStockPrice(String stockWatcher, int count) throws IOException;
}
