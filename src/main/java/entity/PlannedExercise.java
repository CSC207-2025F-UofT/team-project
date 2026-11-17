package entity;

public class PlannedExercise {
    private final Exercise exercise;
    private final int targetSets;
    private final int targetReps;
    private final Double targetWeight; // kg, optional, may be null

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
