package dataaccess;

import entities.DailyAggregate;
import entities.Stock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import usecase.estimateroi.EstimateRoiDataAccessInterface;

/**
 * Supplies closing prices for the Estimate ROI use case from the stock data API.
 */
public class EstimateRoiDataAccessObject implements EstimateRoiDataAccessInterface {
  /** How long after the requested start date the first data point may be (weekends, holidays). */
  private static final int START_TOLERANCE_DAYS = 14;

  private final ApiStockDataAccessObject api;

  /**
   * Creates a data access object for the Estimate ROI use case.
   *
   * @param api The stock data API used to fetch daily aggregates
   */
  public EstimateRoiDataAccessObject(ApiStockDataAccessObject api) {
    this.api = api;
  }

  @Override
  public List<Double> getHistoricalPrices(String ticker, int historyLength) {
    final LocalDate to = LocalDate.now().minusDays(1);
    final LocalDate from = to.minusYears(historyLength);
    final List<DailyAggregate> days =
            new ArrayList<>(api.getDailyAggregates(new Stock(ticker), from, to));
    days.sort(Comparator.comparing(DailyAggregate::getTime));

    // If the first data point is well after the requested start, the stock does not have the
    // full period of history.
    final Instant latestAcceptableStart =
            from.plusDays(START_TOLERANCE_DAYS).atStartOfDay().toInstant(ZoneOffset.UTC);
    if (days.isEmpty() || days.get(0).getTime().isAfter(latestAcceptableStart)) {
      return List.of();
    }

    final List<Double> closes = new ArrayList<>();
    for (DailyAggregate day : days) {
      closes.add(day.getClose());
    }
    return closes;
  }
}
