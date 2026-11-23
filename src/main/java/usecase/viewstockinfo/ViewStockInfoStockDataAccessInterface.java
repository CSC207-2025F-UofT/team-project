package usecase.viewstockinfo;

import entities.DailyAggregate;
import entities.Stock;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Data access interface for fresh stock history and per-ticker cached history.
 */
public interface ViewStockInfoStockDataAccessInterface {
  /**
   * Fetches completed end-of-day aggregates from the stock data provider.
   *
   * @param stock The stock to look up
   * @param from Start date of the requested window, inclusive
   * @param to End date of the requested window, inclusive
   * @return The daily aggregates, or an empty list if the provider has no data
   */
  List<DailyAggregate> getDailyAggregates(Stock stock, LocalDate from, LocalDate to);

  /**
   * Loads the most recent successful one-year history for the specified ticker.
   *
   * @param stock The stock whose normalized ticker identifies the cache entry
   * @return The cached history for this ticker, or an empty Optional if unavailable
   * @throws IOException If a cache entry cannot be read
   */
  Optional<CachedHistory> getCachedDailyAggregates(Stock stock) throws IOException;

  /**
   * Replaces the cache entry for the specified ticker with a successful fetch.
   *
   * @param stock The stock whose normalized ticker identifies the cache entry
   * @param history The window end and nonempty daily aggregates from the successful fetch
   * @throws IOException If the cache entry cannot be saved
   */
  void saveCachedDailyAggregates(Stock stock, CachedHistory history) throws IOException;

  /** The last successful query window and its daily aggregates. */
  final class CachedHistory {
    private final LocalDate endDate;
    private final List<DailyAggregate> dailyAggregates;

    /**
     * Creates a snapshot of the data returned for a one-year query.
     *
     * @param endDate The last date of the original query window
     * @param dailyAggregates The daily aggregates in that window
     */
    public CachedHistory(LocalDate endDate, List<DailyAggregate> dailyAggregates) {
      this.endDate = endDate;
      this.dailyAggregates = List.copyOf(dailyAggregates);
    }

    public LocalDate getEndDate() {
      return endDate;
    }

    public List<DailyAggregate> getDailyAggregates() {
      return dailyAggregates;
    }
  }
}
