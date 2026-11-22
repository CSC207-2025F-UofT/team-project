package entities;

import java.util.List;

/**
 * An entity representing the user's watchlist.
 */
public class Watchlist {
  private final List<Stock> stocks;

  /**
   * Constructs a new Watchlist object.
   *
   * @param stocks The list of stocks in the user's watchlist
   */
  public Watchlist(List<Stock> stocks) {
    this.stocks = stocks;
  }

  public List<Stock> getStocks() {
    return stocks;
  }

  /**
   * Adds a stock to this watchlist.
   *
   * @param stock The stock entity to add
   */
  public void addStock(Stock stock) {
    this.stocks.add(stock);
  }

  /**
   * Removes a stock from this watchlist.
   *
   * @param stock The stock entity to remove
   * @return Whether the stock was present in this watchlist and removed.
   */
  public boolean removeStock(Stock stock) {
    return this.stocks.remove(stock);
  }
}
