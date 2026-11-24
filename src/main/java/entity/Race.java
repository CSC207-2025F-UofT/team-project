package entity;

/**
 * This entity stores information about a race fetched from the DnD5e API. It includes the name of
 * the race and its associated stat bonuses.
 */
public final class Race {
  private final String name;
  private final Stats bonuses;

  /** Creates a race with its name and ability-score bonuses. */
  public Race(String name, Stats bonuses) {
    this.name = name;
    this.bonuses = bonuses;
  }

  /** Returns this race's display name. */
  public String getName() {
    return name;
  }

  /** Returns the ability-score bonuses granted by this race. */
  public Stats getBonuses() {
    return bonuses;
  }

  /** Returns the race name for display in selection controls. */
  @Override
  public String toString() {
    return name;
  }
}
