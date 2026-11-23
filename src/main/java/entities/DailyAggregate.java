package entities;

import java.time.Instant;

/**
 * An entity representing a daily aggregate (a summary of all trading data over a single day).
 * This entity is immutable once created, so it has no mutator methods.
 */
public class DailyAggregate {
  private final double open;
  private final double close;
  private final double high;
  private final double low;
  private final double volume;
  private final double volumeWeightedAveragePrice;
  private final Instant time;

  /**
   * Constructs a new DailyAggregate object.
   *
   * @param open Opening price of the day
   * @param close Closing price of the day
   * @param high Highest price during the day
   * @param low Lowest price during the day
   * @param volume Trading volume during the day
   * @param volumeWeightedAveragePrice Volume-weighted average price during the day
   * @param time The day this object refers to (stored as an Instant object)
   */
  public DailyAggregate(double open, double close, double high, double low, double volume,
                        double volumeWeightedAveragePrice, Instant time) {
    this.open = open;
    this.close = close;
    this.high = high;
    this.low = low;
    this.volume = volume;
    this.volumeWeightedAveragePrice = volumeWeightedAveragePrice;
    this.time = time;
  }

  public double getOpen() {
    return open;
  }

  public double getClose() {
    return close;
  }

  public double getHigh() {
    return high;
  }

  public double getLow() {
    return low;
  }

  public double getVolume() {
    return volume;
  }

  public double getVolumeWeightedAveragePrice() {
    return volumeWeightedAveragePrice;
  }

  public Instant getTime() {
    return time;
  }
}
