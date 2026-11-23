package entities;

/**
 * An entity representing the output of an algorithm that predicts when the user should sell
 * a stock. This entity is immutable once created, so it has no mutator methods.
 */
public class SellEstimate {
  private final int holdPeriod;
  private final double sellPrice;
  private final double error;
  private final double returns;
  private final boolean targetReached;

  /**
   * Creates a new SellEstimate object.
   *
   * @param holdPeriod Holding period of the specified stock in trading days
   * @param error Variability in projected sell price: volatility * sqrt(holding period)
   * @param sellPrice Price of a single share of the stock at the end of the holding period
   * @param returns Dollar returns after selling at the end of the holding period
   * @param targetReached Whether the user's specified target gain was reached
   */
  public SellEstimate(int holdPeriod, double error, double sellPrice, double returns,
                      boolean targetReached) {
    this.holdPeriod = holdPeriod;
    this.error = error;
    this.sellPrice = sellPrice;
    this.returns = returns;
    this.targetReached = targetReached;
  }

  public int getHoldPeriod() {
    return holdPeriod;
  }

  public double getSellPrice() {
    return sellPrice;
  }

  public double getError() {
    return error;
  }

  public double getReturns() {
    return returns;
  }

  public boolean isTargetReached() {
    return targetReached;
  }
}
