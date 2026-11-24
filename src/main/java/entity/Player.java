package entity;

/**
 * The student character. Holds energy and the single activity (challenge or exam) in progress, if any.
 * The academic record is to be added by the team.
 */
public class Player {
    private int energy;
    private final int maxEnergy;
    private String activeAttemptId;

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

    /**
     * Deducts energy. Nothing changes if the amount is rejected.
     * @param amount the energy to spend; must not be negative
     * @throws IllegalArgumentException if the amount is negative
     * @throws IllegalStateException if the player does not have enough energy
     */
    public void spendEnergy(int amount) {
        if (amount < 0) {
            throw new IllegalArgumentException("Energy cost cannot be negative");
        }
        if (amount > energy) {
            throw new IllegalStateException("Not enough energy");
        }
        energy -= amount;
    }

    /**
     * Marks a challenge or exam attempt as in progress. Rest and Continue are not allowed meanwhile.
     * @throws IllegalStateException if another activity is already in progress
     */
    public void startActivity(String attemptId) {
        if (activeAttemptId != null) {
            throw new IllegalStateException("Another activity is in progress");
        }
        activeAttemptId = attemptId;
    }

    /**
     * Ends the given attempt. Repeating it, or passing another attempt's ID, changes nothing.
     * @return whether the given attempt was the active one and has now been released
     */
    public boolean finishActivity(String attemptId) {
        if (activeAttemptId == null || !activeAttemptId.equals(attemptId)) {
            return false;
        }
        activeAttemptId = null;
        return true;
    }

    public boolean hasActiveActivity() {
        return activeAttemptId != null;
    }

    public int getEnergy() {
        return energy;
    }

    public int getMaxEnergy() {
        return maxEnergy;
    }
}
