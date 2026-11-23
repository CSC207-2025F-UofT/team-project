package usecase.estimateroi;

/**
 * The input data for the Estimate ROI use case.
 */
public class EstimateRoiInputData {
  private final String ticker;
  private final double amount;
  private final int historyLength;

  /**
   * Creates the input data for an estimate.
   *
   * @param ticker The ticker of the stock to estimate
   * @param amount The dollar amount to invest
   * @param historyLength How many years of past prices to base the estimate on
   */
  public EstimateRoiInputData(String ticker, double amount, int historyLength) {
    this.ticker = ticker;
    this.amount = amount;
    this.historyLength = historyLength;
  }

  public String getTicker() {
    return ticker;
  }

  public double getAmount() {
    return amount;
  }

  public int getHistoryLength() {
    return historyLength;
  }
}
