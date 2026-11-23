package entities;

import java.util.ArrayList;
import java.util.List;

/**
 * An entity representing a stock's growth statistics over a period of time.
 * This entity is immutable once created, so it has no mutator methods.
 */
public class GrowthStats {
  private final double averageDailyReturn;
  private final double volatility;

  /**
   * Checks whether a given double value is effectively zero. This also accounts for floating-point
   * errors by checking within a small epsilon (tolerance) range around zero.
   *
   * @param x The double value
   * @return Whether the given value is (close to) zero
   */
  private boolean isCloseToZero(double x) {
    double epsilon = 1e-9;
    return Math.abs(x) < epsilon;
  }

  /**
   * Calculates the daily log returns for a given daily price history.
   *
   * @param priceHistory Price history of the stock
   * @return A list containing log returns of the stock each day
   */
  private ArrayList<Double> getLogReturns(List<DailyAggregate> priceHistory) {
    ArrayList<Double> logReturns = new ArrayList<>();
    for (int i = 0; i < priceHistory.size() - 1; i++) {
      double pt = priceHistory.get(i + 1).getClose(); // P_t
      double ptm1 = priceHistory.get(i).getClose(); // P_{t-1}
      if (isCloseToZero(ptm1)) {
        throw new IllegalArgumentException(
                "Close-to-zero closing price detected (possibly due to missing data) - "
                        + "calculations would become unstable");
      }

      logReturns.add(Math.log(pt / ptm1)); // Log return: ln(P_t/P_{t-1})
    }
    return logReturns;
  }

  /**
   * Constructs a new GrowthStats object.
   *
   * @param priceHistory The price history to calculate stats from
   */
  public GrowthStats(List<DailyAggregate> priceHistory) {
    if (priceHistory.size() < 2) {
      throw new IllegalArgumentException("Not enough data in price history to calculate growth");
    }

    ArrayList<Double> logReturns = getLogReturns(priceHistory);

    averageDailyReturn = logReturns.stream()
            .mapToDouble(Double::doubleValue)
            .average()
            .orElseThrow(); // Will not throw since we've already checked for minimum data length.

    double squaredDiffSum = logReturns.stream()
            .mapToDouble(x -> Math.pow(x - averageDailyReturn, 2))
            .sum();

    // Volatility is calculated as sample standard deviation
    volatility = Math.sqrt(squaredDiffSum / (logReturns.size() - 1));
  }

  public double getAverageDailyReturn() {
    return averageDailyReturn;
  }

  public double getVolatility() {
    return volatility;
  }
}
