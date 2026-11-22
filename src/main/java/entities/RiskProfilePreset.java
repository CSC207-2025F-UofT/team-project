package entities;

import java.util.Optional;

/**
 * Predefined risk profiles a user can choose from instead of entering custom values.
 * Holding periods are measured in trading days (about 21 per month, 252 per year).
 */
public enum RiskProfilePreset {
  CONSERVATIVE(0.05, 126),
  MODERATE(0.10, 252),
  AGGRESSIVE(0.25, 504);

  private static final double EPSILON = 1e-9;

  private final double targetGain;
  private final int maxHoldingPeriod;

  RiskProfilePreset(double targetGain, int maxHoldingPeriod) {
    this.targetGain = targetGain;
    this.maxHoldingPeriod = maxHoldingPeriod;
  }

  public double getTargetGain() {
    return targetGain;
  }

  public int getMaxHoldingPeriod() {
    return maxHoldingPeriod;
  }

  /**
   * Creates the risk profile described by this preset.
   *
   * @return A new RiskProfile with this preset's values
   */
  public RiskProfile toRiskProfile() {
    return new RiskProfile(targetGain, maxHoldingPeriod);
  }

  /**
   * Finds the preset whose values are identical to the given risk profile, if there is one.
   *
   * @param profile The risk profile to match
   * @return The matching preset, or an empty Optional if the profile is a custom one
   */
  public static Optional<RiskProfilePreset> matching(RiskProfile profile) {
    for (RiskProfilePreset preset : values()) {
      if (Math.abs(preset.targetGain - profile.getTargetGain()) < EPSILON
              && preset.maxHoldingPeriod == profile.getMaxHoldingPeriod()) {
        return Optional.of(preset);
      }
    }
    return Optional.empty();
  }
}
