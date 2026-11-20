package usecase;

import entity.Stats;

/** Represents the character data returned to the view. */
public class CreateCharacterResponse {
  private final String raceName;
  private final String className;
  private final Stats stats;

  /** Creates output data containing the selected names and calculated stats. */
  public CreateCharacterResponse(String raceName, String className, Stats stats) {
    this.raceName = raceName;
    this.className = className;
    this.stats = stats;
  }

  /** Returns the name of the character's race. */
  public String getRaceName() {
    return raceName;
  }

  /** Returns the name of the character's class. */
  public String getClassName() {
    return className;
  }

  /** Returns the calculated ability scores. */
  public Stats getStats() {
    return stats;
  }
}
