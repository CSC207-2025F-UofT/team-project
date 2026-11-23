package usecase.viewstockinfo;

/**
 * The View Stock Info use case.
 */
public interface ViewStockInfoInputBoundary {
  /** Loads the saved stocks available to view. */
  void loadWatchlist();

  /**
   * Loads end-of-day stock data for the table and the fixed one-year price chart.
   *
   * @param inputData The stock to look up
   */
  void execute(ViewStockInfoInputData inputData);
}
