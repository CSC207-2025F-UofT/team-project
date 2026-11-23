package entity;

/**
 * The student character. Currently holds only energy; the academic record is to be added by the team.
 */
public class Player {
    private int energy;
    private final int maxEnergy;

    public Player(int energy, int maxEnergy) {
        this.energy = energy;
        this.maxEnergy = maxEnergy;
    }

    /**
     * Raises energy to the given percent of the maximum. Energy that is already higher is kept.
     * @param percent the percent of maximum energy to restore to
     */
    public void restoreEnergyToPercent(int percent) {
        energy = Math.max(energy, maxEnergy * percent / 100);
    }

    public int getEnergy() {
        return energy;
    }

    public int getMaxEnergy() {
        return maxEnergy;
    }
}
