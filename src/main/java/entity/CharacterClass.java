package entity;

/**
 * This entity stores information about a character class fetched from the DnD5e API. It includes
 * the name of the character class and its base stats.
 */
public final class CharacterClass {
  private final String name;
  private final Stats baseStats;

  /**
   * Creates a character class.
   *
   * @param name the name of the character class
   * @param baseStats the base stats of the character class
   */
  public CharacterClass(String name, Stats baseStats) {
    this.name = name;
    this.baseStats = baseStats;
  }

  /** Returns this class's display name. */
  public String getName() {
    return name;
  }

  /** Returns the ability scores associated with this class. */
  public Stats getBaseStats() {
    return baseStats;
  }

  /** Returns the class name for display in selection controls. */
  @Override
  public String toString() {
    return name;
  }
}
