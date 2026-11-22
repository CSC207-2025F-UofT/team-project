package use_case.watchlist;

import java.io.IOException;
import java.util.List;

public interface WatchlistStockDataAccessInterface {

    List<Double> getStockPrice(String stockWatcher, int count) throws IOException;
}
