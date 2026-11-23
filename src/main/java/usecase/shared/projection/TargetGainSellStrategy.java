package usecase.shared.projection;

import entities.GrowthStats;
import entities.RiskProfile;
import entities.SellEstimate;

/**
 * Estimator that calculates the holding period and sell date of a stock from the user's target
 * gain of the stock.
 */
public class TargetGainSellStrategy implements SellStrategy {
  /**
   * Calculates the projected price of a stock t days from now.
   *
   * @param startPrice Starting price of one share
   * @param mu Average daily log return
   * @param t Number of days in holding period
   * @return Projected price of one share after the specified holding period
   */
  private double getProjectedPrice(double startPrice, double mu, int t) {
    return startPrice * Math.exp(mu * t);
  }

  /**
   * Calculates the dollar returns of a stock.
   *
   * @param startPrice Starting price of one share
   * @param endPrice Ending (when sold) price of one share
   * @param amount Dollar amount invested
   * @return Dollar amount gained or lost after selling
   */
  private double getReturns(double startPrice, double endPrice, double amount) {
    int shares = (int) (amount / startPrice); // Only whole shares can be bought
    return shares * (endPrice - startPrice);
  }

  @Override
  public SellEstimate estimate(double amount, double startPrice, GrowthStats stats,
                               RiskProfile profile) {
    double targetGain = profile.getTargetGain();
    double mu = stats.getAverageDailyReturn();
    int maxHoldPeriod = profile.getMaxHoldingPeriod();

    int holdPeriod = maxHoldPeriod;
    boolean targetHit = false;
    if (mu > 0) {
      // Result of solving P_0 * e^(mu*t) = P_0(1 + g) for t
      double daysToTarget = Math.ceil(Math.log(1 + targetGain) / mu);
      if (daysToTarget <= maxHoldPeriod) {
        holdPeriod = (int) daysToTarget;
        targetHit = true;
      }
    }

    double projectedPrice = getProjectedPrice(startPrice, mu, holdPeriod);
    double error = stats.getVolatility() * Math.sqrt(holdPeriod);
    double returns = getReturns(startPrice, projectedPrice, amount);
    return new SellEstimate(holdPeriod, error, projectedPrice, returns, targetHit);
  }
}
