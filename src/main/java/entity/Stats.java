package entity;

/**
 * This entity stores the six ability scores (strength, dexterity, constitution, intelligence,
 * wisdom, and charisma) together.
 * It's a final class because mutating the attributes might interfere with the cache of the API.
 * If stats get modified then it's better to use {@code plus} to create a new Stats object
 */
public final class Stats {
  private final int strength;
  private final int dexterity;
  private final int constitution;
  private final int intelligence;
  private final int wisdom;
  private final int charisma;

  /** Creates a set of six ability scores. */
  public Stats(
      int strength, int dexterity, int constitution, int intelligence, int wisdom, int charisma) {
    this.strength = strength;
    this.dexterity = dexterity;
    this.constitution = constitution;
    this.intelligence = intelligence;
    this.wisdom = wisdom;
    this.charisma = charisma;
  }

  /** Returns the Strength score. */
  public int getStrength() {
    return strength;
  }

  /** Returns the Dexterity score. */
  public int getDexterity() {
    return dexterity;
  }

  /** Returns the Constitution score. */
  public int getConstitution() {
    return constitution;
  }

  /** Returns the Intelligence score. */
  public int getIntelligence() {
    return intelligence;
  }

  /** Returns the Wisdom score. */
  public int getWisdom() {
    return wisdom;
  }

  /** Returns the Charisma score. */
  public int getCharisma() {
    return charisma;
  }

  /**
   * Returns a new {@code Stats} object where the value of each attribute is the sum of the current
   * instance and {@code other}.
   *
   * @param other the stats to be added with the current instance
   * @return the stats after adding {@code other} and the current instance together
   */
  public Stats plus(Stats other) {
    return new Stats(
        strength + other.strength,
        dexterity + other.dexterity,
        constitution + other.constitution,
        intelligence + other.intelligence,
        wisdom + other.wisdom,
        charisma + other.charisma);
  }

  /** Formats the six ability scores as a readable string. */
  @Override
  public String toString() {
    return "STR "
        + strength
        + ", DEX "
        + dexterity
        + ", CON "
        + constitution
        + ", INT "
        + intelligence
        + ", WIS "
        + wisdom
        + ", CHA "
        + charisma;
  }
}
