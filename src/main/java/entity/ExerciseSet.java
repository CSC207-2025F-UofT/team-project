package entity;

/**
 * One set of an exercise that was actually performed: the weight lifted and
 * the number of repetitions done.
 */
public class ExerciseSet {
    private final double weight; // kg
    private final int reps;

    /**
     * Creates a completed set.
     *
     * @param weight the weight lifted, in kilograms
     * @param reps   the number of repetitions done
     */
    public ExerciseSet(double weight, int reps) {
        this.weight = weight;
        this.reps = reps;
    }

    public double getWeight() { return weight; }
    public int getReps() { return reps; }
}
