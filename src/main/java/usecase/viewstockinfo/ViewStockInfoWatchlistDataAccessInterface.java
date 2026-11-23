package usecase.viewstockinfo;

import entities.Stock;
import java.io.IOException;
import java.util.List;

/** Reads the stocks that the user has saved to their watchlist. */
public interface ViewStockInfoWatchlistDataAccessInterface {
  /**
   * Loads the watchlist.
   *
   * @return The saved stocks, or an empty list if no watchlist has been saved
   * @throws IOException If the watchlist cannot be read
   */
  List<Stock> getWatchlistStocks() throws IOException;
}
