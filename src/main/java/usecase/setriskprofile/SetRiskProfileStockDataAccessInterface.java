package usecase.setriskprofile;

import entities.DailyAggregate;
import entities.Stock;
import java.time.LocalDate;
import java.util.ArrayList;

/**
 * Data access interface for fetching stock price information.
 */
public interface SetRiskProfileStockDataAccessInterface {
  /**
   * Accesses the daily aggregates of a stock across a specified time period.
   *
   * @param stock The stock to look up data for
   * @param from Start day of the time period
   * @param to End day of the time period
   * @return A list of aggregate objects for each day in the specified range
   */
  ArrayList<DailyAggregate> getDailyAggregates(Stock stock, LocalDate from, LocalDate to);
}
