package usecase;

/** Represents the character data returned to the view. */
public class CreateCharacterResponse {
  private final String raceName;
  private final String className;
  private final int strength;
  private final int dexterity;
  private final int intelligence;
  private final int constitution;
  private final int wisdom;
  private final int charisma;

  /** Creates output data containing the selected names and calculated ability scores. */
  public CreateCharacterResponse(
      String raceName,
      String className,
      int strength,
      int dexterity,
      int intelligence,
      int constitution,
      int wisdom,
      int charisma) {
    this.raceName = raceName;
    this.className = className;
    this.strength = strength;
    this.dexterity = dexterity;
    this.intelligence = intelligence;
    this.constitution = constitution;
    this.wisdom = wisdom;
    this.charisma = charisma;
  }

  /** Returns the name of the character's race. */
  public String getRaceName() {
    return raceName;
  }

  /** Returns the name of the character's class. */
  public String getClassName() {
    return className;
  }

  /** Returns the calculated Strength score. */
  public int getStrength() {
    return strength;
  }

  /** Returns the calculated Dexterity score. */
  public int getDexterity() {
    return dexterity;
  }

  /** Returns the calculated Intelligence score. */
  public int getIntelligence() {
    return intelligence;
  }

  /** Returns the calculated Constitution score. */
  public int getConstitution() {
    return constitution;
  }

  /** Returns the calculated Wisdom score. */
  public int getWisdom() {
    return wisdom;
  }

  /** Returns the calculated Charisma score. */
  public int getCharisma() {
    return charisma;
  }
}
