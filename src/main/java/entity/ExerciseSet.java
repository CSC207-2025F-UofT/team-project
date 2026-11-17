package entity;

public class ExerciseSet {
    private final double weight; // kg
    private final int reps;

    public ExerciseSet(double weight, int reps) {
        this.weight = weight;
        this.reps = reps;
    }

    public double getWeight() { return weight; }
    public int getReps() { return reps; }
}
