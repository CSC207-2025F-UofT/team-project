package entity;

/**
 * An exercise in a {@link WorkoutPlan}, with the sets, reps and weight the
 * user aims for.
 */
public class PlannedExercise {
    private final Exercise exercise;
    private final int targetSets;
    private final int targetReps;
    private final Double targetWeight; // kg, optional, may be null

    /**
     * Creates a planned exercise.
     *
     * @param exercise     the exercise to do
     * @param targetSets   the number of sets to aim for
     * @param targetReps   the number of repetitions to aim for in each set
     * @param targetWeight the weight to aim for, in kilograms, or {@code null} if none is set
     *                     (for example, for bodyweight exercises)
     */
    public PlannedExercise(Exercise exercise, int targetSets, int targetReps, Double targetWeight) {
        this.exercise = exercise;
        this.targetSets = targetSets;
        this.targetReps = targetReps;
        this.targetWeight = targetWeight;
    }

    public Exercise getExercise() { return exercise; }
    public int getTargetSets() { return targetSets; }
    public int getTargetReps() { return targetReps; }
    public Double getTargetWeight() { return targetWeight; }
}
