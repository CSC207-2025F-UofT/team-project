package usecase.viewstockinfo;

import java.util.List;

/**
 * Output boundary for the View Stock Info use case.
 */
public interface ViewStockInfoOutputBoundary {
  /**
   * Displays the saved stocks available to view.
   *
   * @param tickers Tickers in the user's watchlist
   */
  void prepareWatchlistView(List<String> tickers);

  /**
   * Displays an error encountered while loading the watchlist.
   *
   * @param errorMessage Explanation of the failure
   */
  void prepareWatchlistFailView(String errorMessage);

  /**
   * Prepares the table and chart, indicating when the result comes from cache.
   *
   * @param outputData Fresh data or the cached data for the requested stock
   */
  void prepareSuccessView(ViewStockInfoOutputData outputData);

  /**
   * Prepares the failure view for invalid input or unavailable stock data.
   *
   * @param errorMessage Explanation of the failure
   */
  void prepareFailView(String errorMessage);
}
