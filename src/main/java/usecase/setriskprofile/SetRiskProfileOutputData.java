package usecase.setriskprofile;

import java.time.LocalDate;

/**
 * Output data for the Set Risk Profile use case.
 */
public class SetRiskProfileOutputData {
  private final String ticker;
  private final double startPrice;
  private final LocalDate sellDate;
  private final int holdPeriod;
  private final double sellPrice;
  private final double error;
  private final double returns;
  private final boolean targetReached;

  /**
   * Creates a new SetRiskProfileOutputData object.
   *
   * @param ticker Ticker of the stock that was estimated
   * @param startPrice Price of a single share of the stock when bought (latest close)
   * @param sellDate Calendar day to sell the stock
   * @param holdPeriod Holding period in trading days
   * @param sellPrice Price of a single share of the stock when sold
   * @param error Variability in projected sell price (standard deviation of the log return)
   * @param returns Dollar returns after selling at the end of the holding period
   * @param targetReached Whether the user's specified target gain was reached
   */
  public SetRiskProfileOutputData(String ticker, double startPrice, LocalDate sellDate,
                                  int holdPeriod, double sellPrice, double error, double returns,
                                  boolean targetReached) {
    this.ticker = ticker;
    this.startPrice = startPrice;
    this.sellDate = sellDate;
    this.holdPeriod = holdPeriod;
    this.sellPrice = sellPrice;
    this.error = error;
    this.returns = returns;
    this.targetReached = targetReached;
  }

  public String getTicker() {
    return ticker;
  }

  public double getStartPrice() {
    return startPrice;
  }

  public LocalDate getSellDate() {
    return sellDate;
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
