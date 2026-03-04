package entity;


public class Spells {
    private final String spellName;
    private final int DMG;

    /**
     * This constructor of the Spells class contains the type of the damage(spellName), and the value of the corresponding damage.
     */
    public Spells(String spellName, int DMG) {
        this.spellName = spellName;
        this.DMG = DMG;
    }

    // TODO: finish the override
    @Override
    public String toString() {
        return spellName;
    }

    /**
     * The getter for spellName.
     */
    public String getSpellName() {return spellName;}

    /**
     * The getter for spell's DMG.
     */
    public int getDMG() {return DMG;}

}
