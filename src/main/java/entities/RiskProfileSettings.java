package entities;

/**
 * An entity representing the user's saved estimation settings: their risk profile and the window
 * of past prices used to estimate future performance.
 * This entity is immutable once created, so it has no mutator methods.
 */
public class RiskProfileSettings {
  private final RiskProfile riskProfile;
  private final int priceWindowMonths;

  /**
   * Constructs a new RiskProfileSettings object.
   *
   * @param riskProfile The user's risk profile
   * @param priceWindowMonths Number of past months of prices to estimate from
   */
  public RiskProfileSettings(RiskProfile riskProfile, int priceWindowMonths) {
    if (riskProfile == null) {
      throw new IllegalArgumentException("Risk profile cannot be null");
    }
    if (priceWindowMonths <= 0) {
      throw new IllegalArgumentException("Past price window must be positive");
    }

    this.riskProfile = riskProfile;
    this.priceWindowMonths = priceWindowMonths;
  }

  public RiskProfile getRiskProfile() {
    return riskProfile;
  }

  public int getPriceWindowMonths() {
    return priceWindowMonths;
  }
}
