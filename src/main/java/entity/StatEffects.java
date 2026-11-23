package entity;

/**
 * An immutable set of changes to the player's four stats.
 * Positive numbers increase a stat, negative numbers decrease it.
 */
public final class StatEffects {

    public static final StatEffects NONE = new StatEffects(0, 0, 0, 0);

    private final int energyDelta;
    private final int sanityDelta;
    private final int academicDelta;
    private final int moneyDelta;

    public StatEffects(int energyDelta, int sanityDelta, int academicDelta, int moneyDelta) {
        this.energyDelta = energyDelta;
        this.sanityDelta = sanityDelta;
        this.academicDelta = academicDelta;
        this.moneyDelta = moneyDelta;
    }

    public int getEnergyDelta() {
        return energyDelta;
    }

    public int getSanityDelta() {
        return sanityDelta;
    }

    public int getAcademicDelta() {
        return academicDelta;
    }

    public int getMoneyDelta() {
        return moneyDelta;
    }
}