package usecase.shared.projection;

import entities.GrowthStats;
import entities.RiskProfile;
import entities.SellEstimate;

/**
 * Interface describing an algorithm to estimate the sell date of a stock given its stats and
 * the user's risk profile.
 */
public interface SellStrategy {
  /**
   * Estimates the holding period and expected return of a stock.
   *
   * @param amount Dollar amount invested
   * @param startPrice Starting price of one share
   * @param stats Growth statistics of the stock in the past
   * @param profile The user's risk profile
   * @return An estimate of the holding period and return
   */
  SellEstimate estimate(double amount, double startPrice, GrowthStats stats, RiskProfile profile);
}
