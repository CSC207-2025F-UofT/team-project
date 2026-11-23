package usecase.estimateroi;

/**
 * The output data for the Estimate ROI use case.
 */
public class EstimateRoiOutputData {
  private final double estimatedValue;
  private final double profit;
  private final double percentageReturn;
  private final int historyLength;

  /**
   * Creates the output data for a successful estimate.
   *
   * @param estimatedValue The estimated value of the investment after one year
   * @param profit The estimated profit (negative for a loss) in dollars
   * @param percentageReturn The estimated return as a percentage
   * @param historyLength How many years of past prices the estimate is based on
   */
  public EstimateRoiOutputData(double estimatedValue, double profit, double percentageReturn,
                               int historyLength) {
    this.estimatedValue = estimatedValue;
    this.profit = profit;
    this.percentageReturn = percentageReturn;
    this.historyLength = historyLength;
  }

  public double getEstimatedValue() {
    return estimatedValue;
  }

  public double getProfit() {
    return profit;
  }

  public double getPercentageReturn() {
    return percentageReturn;
  }

  public int getHistoryLength() {
    return historyLength;
  }
}
