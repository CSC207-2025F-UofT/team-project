package usecase.viewstockinfo;

/**
 * Input data for the View Stock Info use case. The interactor validates the ticker.
 */
public class ViewStockInfoInputData {
  private final String ticker;

  /**
   * Creates a new ViewStockInfoInputData object.
   *
   * @param ticker Ticker of the stock to look up
   */
  public ViewStockInfoInputData(String ticker) {
    this.ticker = ticker;
  }

  public String getTicker() {
    return ticker;
  }
}
