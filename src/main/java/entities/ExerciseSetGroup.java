package entities;

import java.util.ArrayList;
import java.util.List;

/**
 * A group of sets for a single exercise within a template, e.g. "Bench Press: 3 sets".
 * Each set has a target weight and a target rep count.
 */
public class ExerciseSetGroup {
    private final Exercise exercise;
    private int setCount = 0;
    private final List<Double> weights = new ArrayList<>();
    private final List<Integer> reps = new ArrayList<>();

    public ExerciseSetGroup(Exercise exercise){
        this.exercise = exercise;
    }

    /**
     * Creates a deep copy of another group, so that changes to the copy
     * (e.g. during an ongoing workout) do not affect the original template.
     */
    public ExerciseSetGroup(ExerciseSetGroup other){
        this.exercise = other.exercise;
        this.setCount = other.setCount;
        this.weights.addAll(other.weights);
        this.reps.addAll(other.reps);
    }

    public void addSet(double weight, int reps){
        this.weights.add(weight);
        this.reps.add(reps);
        this.setCount++;
    }

    public void removeSet(int index){
        this.weights.remove(index);
        this.reps.remove(index);
        this.setCount--;
    }

    public Exercise getExercise(){
        return this.exercise;
    }

    public int getSetCount(){
        return setCount;
    }

    public double getWeight(int setNumber){
        return this.weights.get(setNumber);
    }

    public int getReps(int setNumber){
        return this.reps.get(setNumber);
    }

    public void setWeight(int setNumber, double weight){
        this.weights.set(setNumber, weight);
    }

    public void setReps(int setNumber, int reps){
        this.reps.set(setNumber, reps);
    }

    public List<Double> getWeights(){
        return List.copyOf(this.weights);
    }

    public List<Integer> getRepsList(){
        return List.copyOf(this.reps);
    }
}
