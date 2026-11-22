package entities;

/**
 * An entity representing a risk profile for a user.
 * This entity is immutable once created, so it has no mutator methods.
 */
public class RiskProfile {
  private final double targetGain;
  private final int maxHoldingPeriod;

  /**
   * Constructs a new RiskProfile object.
   *
   * @param targetGain Percentage (e.g., 20% would be 0.2) the user wants to gain from the stock
   * @param maxHoldingPeriod Maximum number of trading days the user wants to hold
   */
  public RiskProfile(double targetGain, int maxHoldingPeriod) {
    if (targetGain <= 0) {
      throw new IllegalArgumentException("Target gain must be positive");
    }
    if (maxHoldingPeriod <= 0) {
      throw new IllegalArgumentException("Maximum holding period must be positive");
    }

    this.targetGain = targetGain;
    this.maxHoldingPeriod = maxHoldingPeriod;
  }

  public double getTargetGain() {
    return targetGain;
  }

  public int getMaxHoldingPeriod() {
    return maxHoldingPeriod;
  }
}
