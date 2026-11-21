package entities;

import java.util.List;


public class LogExerciseSet {
    private final String exerciseName;
    private final List<Double> weights;
    private final List<Integer> reps;

    public LogExerciseSet(String exerciseName, List<Double> weights, List<Integer> reps){
        if (weights.size() != reps.size()){
            throw new IllegalArgumentException("weights and reps must have the same number of sets");
        }
        this.exerciseName = exerciseName;
        this.weights = List.copyOf(weights);
        this.reps = List.copyOf(reps);
    }

    public String getExerciseName(){
        return exerciseName;
    }

    public List<Double> getWeights(){
        return weights;
    }

    public List<Integer> getReps(){
        return reps;
    }

    public int getSetCount(){
        return weights.size();
    }

    public double getWeight(int setNumber){
        return weights.get(setNumber);
    }

    public int getReps(int setNumber){
        return reps.get(setNumber);
    }
}
