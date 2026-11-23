package usecase.estimateroi;

import java.util.List;

/**
 * Data access interface for the Estimate ROI use case.
 */
public interface EstimateRoiDataAccessInterface {
  /**
   * Gets the daily closing prices of a stock over a past period.
   *
   * @param ticker The ticker of the stock
   * @param historyLength How many years of history to return
   * @return The closing prices ordered from oldest to newest, or an empty list if the stock does
   *     not have the full period of history
   */
  List<Double> getHistoricalPrices(String ticker, int historyLength);
}
