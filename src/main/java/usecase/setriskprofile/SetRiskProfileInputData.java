package usecase.setriskprofile;

/**
 * Input data for the Set Risk Profile use case. Validation is done by the interactor so that
 * invalid input is reported through the presenter.
 */
public class SetRiskProfileInputData {
  private final String ticker;
  private final double amount;
  private final double targetGain;
  private final int maxHoldingPeriod;
  private final int window;

  /**
   * Creates a new SetRiskProfileInputData object.
   *
   * @param ticker Ticker of the stock to calculate for
   * @param amount Starting amount in dollars
   * @param targetGain Target gain as a fraction (e.g., 20% would be 0.2)
   * @param maxHoldingPeriod Maximum number of trading days the user wants to hold
   * @param window Number of past months to use to calculate future performance
   */
  public SetRiskProfileInputData(String ticker, double amount, double targetGain,
                                 int maxHoldingPeriod, int window) {
    this.ticker = ticker;
    this.amount = amount;
    this.targetGain = targetGain;
    this.maxHoldingPeriod = maxHoldingPeriod;
    this.window = window;
  }

  public String getTicker() {
    return ticker;
  }

  public double getAmount() {
    return amount;
  }

  public double getTargetGain() {
    return targetGain;
  }

  public int getMaxHoldingPeriod() {
    return maxHoldingPeriod;
  }

  public int getWindow() {
    return window;
  }
}
