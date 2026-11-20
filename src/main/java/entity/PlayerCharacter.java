package entity;

/**
 * This entity represents the player character. It contains attributes like the player's chosen
 * race, character class, and stats.
 */
public class PlayerCharacter {
  private final Race race;
  private final CharacterClass characterClass;

  /**
   * Creates a player character.
   *
   * @param race the race of the character
   * @param characterClass the character class of the character
   */
  public PlayerCharacter(Race race, CharacterClass characterClass) {
    this.race = race;
    this.characterClass = characterClass;
  }

  /**
   * Returns the player character's stats. Stats are calculated as the sum of base stats (determined
   * by character class) and stat bonuses (determined by race).
   *
   * @return the stats of the player character
   */
  public Stats totalStats() {
    return characterClass.getBaseStats().plus(race.getBonuses());
  }
}
