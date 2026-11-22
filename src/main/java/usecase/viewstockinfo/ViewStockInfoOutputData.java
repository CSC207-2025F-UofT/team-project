package usecase.viewstockinfo;

import entities.DailyAggregate;
import java.time.LocalDate;
import java.util.List;

/**
 * Output data for the end-of-day table and the one-year closing-price chart.
 */
public class ViewStockInfoOutputData {
  private final String ticker;
  private final LocalDate from;
  private final LocalDate to;
  private final List<DailyAggregate> dailyAggregates;
  private final List<PricePoint> priceHistory;
  private final Double dailyChange;
  private final Double dailyChangePercent;
  private final boolean fromCache;

  /**
   * Creates a new ViewStockInfoOutputData object.
   *
   * @param ticker Ticker of the stock shown
   * @param from First date of the displayed one-year window, inclusive
   * @param to Last date of the displayed one-year window, inclusive
   * @param dailyAggregates Completed daily aggregates in the same order as the price points
   * @param priceHistory Closing-price points in ascending trading-date order
   * @param dailyChange Change from the previous close, or null if unavailable
   * @param dailyChangePercent Change as a percentage of the previous close, or null if unavailable
   * @param fromCache Whether the result uses the requested stock's cached data
   */
  public ViewStockInfoOutputData(String ticker, LocalDate from, LocalDate to,
                                 List<DailyAggregate> dailyAggregates,
                                 List<PricePoint> priceHistory, Double dailyChange,
                                 Double dailyChangePercent, boolean fromCache) {
    this.ticker = ticker;
    this.from = from;
    this.to = to;
    this.dailyAggregates = List.copyOf(dailyAggregates);
    this.priceHistory = List.copyOf(priceHistory);
    this.dailyChange = dailyChange;
    this.dailyChangePercent = dailyChangePercent;
    this.fromCache = fromCache;
  }

  public String getTicker() {
    return ticker;
  }

  public LocalDate getFrom() {
    return from;
  }

  public LocalDate getTo() {
    return to;
  }

  public List<DailyAggregate> getDailyAggregates() {
    return dailyAggregates;
  }

  public List<PricePoint> getPriceHistory() {
    return priceHistory;
  }

  public Double getDailyChange() {
    return dailyChange;
  }

  public Double getDailyChangePercent() {
    return dailyChangePercent;
  }

  public boolean isFromCache() {
    return fromCache;
  }
}
