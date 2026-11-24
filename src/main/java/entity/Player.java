package entity;

/**
 * The student's four stats. Player is the only class that changes them,
 * and it always keeps them in range: Energy, Sanity and Academic Survival
 * stay between 0 and 100, and Money never goes below 0.
 */
public class Player {

    public static final int MIN_STAT = 0;
    public static final int MAX_STAT = 100;

    public static final int START_ENERGY = 100;
    public static final int START_SANITY = 100;
    public static final int START_ACADEMIC = 50;
    public static final int START_MONEY = 50;

    private int energy;
    private int sanity;
    private int academicSurvival;
    private int money;

    public Player(int energy, int sanity, int academicSurvival, int money) {
        this.energy = clampStat(energy);
        this.sanity = clampStat(sanity);
        this.academicSurvival = clampStat(academicSurvival);
        this.money = Math.max(0, money);
    }

    /** A player with the starting stats for a new game. */
    public static Player createNew() {
        return new Player(START_ENERGY, START_SANITY, START_ACADEMIC, START_MONEY);
    }

    public void applyEffects(StatEffects effects) {
        energy = clampStat(energy + effects.getEnergyDelta());
        sanity = clampStat(sanity + effects.getSanityDelta());
        academicSurvival = clampStat(academicSurvival + effects.getAcademicDelta());
        money = Math.max(0, money + effects.getMoneyDelta());
    }

    /** True if Energy, Sanity or Academic Survival has hit 0 (the player loses). */
    public boolean isOutOfStat() {
        return energy == MIN_STAT || sanity == MIN_STAT || academicSurvival == MIN_STAT;
    }

    private static int clampStat(int value) {
        return Math.max(MIN_STAT, Math.min(MAX_STAT, value));
    }

    public int getEnergy() {
        return energy;
    }

    public int getSanity() {
        return sanity;
    }

    public int getAcademicSurvival() {
        return academicSurvival;
    }

    public int getMoney() {
        return money;
    }
}