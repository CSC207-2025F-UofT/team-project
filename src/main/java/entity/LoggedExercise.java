package entity;

import java.util.List;

/**
 * An exercise as it was actually done in a {@link Workout}, with the sets
 * that were completed.
 */
public class LoggedExercise {
    private final Exercise exercise;
    private final List<ExerciseSet> completedSets;

    /**
     * Creates a logged exercise. The list of sets is copied, so changing or reusing the
     * caller's list afterwards does not change what was logged.
     *
     * @param exercise      the exercise that was done
     * @param completedSets the sets that were completed, in order
     * @throws NullPointerException if the list of sets is null or contains null
     */
    public LoggedExercise(Exercise exercise, List<ExerciseSet> completedSets) {
        this.exercise = exercise;
        this.completedSets = List.copyOf(completedSets);
    }

    public Exercise getExercise() { return exercise; }
    /** Returns the completed sets in order; the list cannot be modified. */
    public List<ExerciseSet> getCompletedSets() { return completedSets; }
}
