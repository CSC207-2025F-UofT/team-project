package entity;

import java.util.List;

public class LoggedExercise {
    private final Exercise exercise;
    private final List<ExerciseSet> completedSets;

    public LoggedExercise(Exercise exercise, List<ExerciseSet> completedSets) {
        this.exercise = exercise;
        this.completedSets = completedSets;
    }

    public Exercise getExercise() { return exercise; }
    public List<ExerciseSet> getCompletedSets() { return completedSets; }
}
