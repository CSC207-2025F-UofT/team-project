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
     * Creates a logged exercise.
     *
     * @param exercise      the exercise that was done
     * @param completedSets the sets that were completed, in order
     */
    public LoggedExercise(Exercise exercise, List<ExerciseSet> completedSets) {
        this.exercise = exercise;
        this.completedSets = completedSets;
    }

    public Exercise getExercise() { return exercise; }
    public List<ExerciseSet> getCompletedSets() { return completedSets; }
}
