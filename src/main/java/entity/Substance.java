package entity;

/**
 * A chemical used in the lab (e.g. water, ether, NaOH, aspirin) and its properties. Substances
 * never change and are stored in a layer.
 */
public final class Substance {
  private final String name;
  private final String formula;
  private final double molarMass;
  private final double pka;
  private final Range meltingRange;
  private final Phase phase;

  /**
   * For acids, creates a substance with its name, formula, molar mass, pKa, and melting range.
   */
  public Substance(String name, String formula, double molarMass,
      double pka, Range meltingRange, Phase phase) {
    this.name = name;
    this.formula = formula;
    this.molarMass = molarMass;
    this.pka = pka;
    this.meltingRange = meltingRange;
    this.phase = phase;
  }

  /**
   * For non-acids, creates a substance with its name, formula,
   * molar mass, and melting range. pKa is NaN.
   */
  public Substance(String name, String formula, double molarMass,
      Range meltingRange, Phase phase) {
    this.name = name;
    this.formula = formula;
    this.molarMass = molarMass;
    this.pka = Double.NaN;
    this.meltingRange = meltingRange;
    this.phase = phase;
  }

  public String getName() {
    return name;
  }

  public String getFormula() {
    return formula;
  }

  public double getMolarMass() {
    return molarMass;
  }

  public double getPka() {
    return pka;
  }

  public Range getMeltingRange() {
    return meltingRange;
  }

  public Phase getPhase() {
    return phase;
  }

  /**
   * Whether this substance dissolves in a phase at a given pH. An acid also dissolves in water
   * when the pH is above its pKa, because it becomes its salt.
   *
   * @param p the phase to check
   * @param ph the pH of the mixture
   * @return true if it dissolves there
   */
  public boolean isSolubleIn(Phase p, double ph) {
    if (isAcid() && p == Phase.AQUEOUS && ph > pka) {
      return true;
    }
    return p == getPhase();
  }

  /**
   * Whether this substance is an acid a base can turn into a salt.
   *
   * @return true if it has a pKa
   */
  public boolean isAcid() {
    return !Double.isNaN(pka);
  }
}