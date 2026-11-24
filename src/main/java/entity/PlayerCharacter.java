package entity;

import static java.lang.Math.max;

/**
 * This entity represents the player character. It contains attributes like the player's chosen
 * race, character class, and stats.
 */
public class PlayerCharacter {
    private String name;
    private int health;
    private int maxHealth;
    private final Race race;
    private final CharacterClass characterClass;
    private final Stats stats;

  /**
   * Creates a player character.
   *
   * @param race the race of the character
   * @param characterClass the character class of the character
   */
  public PlayerCharacter(Race race, CharacterClass characterClass) {
      this.race = race;
      this.characterClass = characterClass;
      this.stats = characterClass.getBaseStats().plus(race.getBonuses());
      this.maxHealth = this.stats.getConstitution() * 3;
      this.health = this.maxHealth;
  }

    public void takeDamage(int damage) { health = max(0, health - damage);}

    public boolean isAlive() { return health > 0;}

    public int getHealth() {return health;}
  /**
   * Returns the player character's saved stats.
   *
   * @return the stats of the player character
   */
  public Stats getStats() {
    return stats;
  }
}
