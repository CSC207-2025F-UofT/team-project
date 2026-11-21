package entities;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * A workout currently in progress, started from a template.
 * Tracks which sets have been completed. The groups are copied from the
 * template so that adjustments made mid-workout do not modify the template.
 */
public class OngoingWorkout {
    private final String templateName;
    private final LocalDateTime startTime;
    private final List<ExerciseSetGroup> groups;
    private final List<List<Boolean>> setCompleted;

    public OngoingWorkout(Template template){
        this(template, LocalDateTime.now());
    }

    public OngoingWorkout(Template template, LocalDateTime startTime){
        int groupNum = template.getGroupCount();

        this.templateName = template.getName();
        this.startTime = startTime;
        this.groups = new ArrayList<>(groupNum);
        this.setCompleted = new ArrayList<>(groupNum);

        // fill lists with initial values
        for (int i = 0; i < groupNum; i++){
            ExerciseSetGroup group = new ExerciseSetGroup(template.getGroup(i));
            this.groups.add(group);

            List<Boolean> newCompletions = new ArrayList<>();
            for (int j = 0; j < group.getSetCount(); j++) newCompletions.add(false);
            this.setCompleted.add(newCompletions);
        }
    }

    public void completeSet(int groupNum, int setNum){
        this.setCompleted.get(groupNum).set(setNum, true);
    }

    public void resetSet(int groupNum, int setNum){
        this.setCompleted.get(groupNum).set(setNum, false);
    }

    public boolean isSetCompleted(int groupNum, int setNum){
        return this.setCompleted.get(groupNum).get(setNum);
    }

    /** Adds an extra set to a group mid-workout. */
    public void addSet(int groupNum, double weight, int reps){
        this.groups.get(groupNum).addSet(weight, reps);
        this.setCompleted.get(groupNum).add(false);
    }

    /** Removes a set from a group mid-workout. */
    public void removeSet(int groupNum, int setNum){
        this.groups.get(groupNum).removeSet(setNum);
        this.setCompleted.get(groupNum).remove(setNum);
    }

    /** Updates the actual weight/reps performed for a set. */
    public void updateSet(int groupNum, int setNum, double weight, int reps){
        ExerciseSetGroup group = this.groups.get(groupNum);
        group.setWeight(setNum, weight);
        group.setReps(setNum, reps);
    }

    public ExerciseSetGroup getGroup(int index){
        return this.groups.get(index);
    }

    public int getGroupCount(){
        return this.groups.size();
    }

    public String getTemplateName(){
        return templateName;
    }

    public LocalDateTime getStartTime(){
        return startTime;
    }

    /** @return true if every set in every group has been completed. */
    public boolean isFinished(){
        for (List<Boolean> completions : this.setCompleted){
            for (boolean done : completions){
                if (!done) return false;
            }
        }
        return true;
    }

    /**
     * Converts this workout into a log entry containing only the completed sets.
     * Exercises with no completed sets are omitted.
     */
    public LogEntry toLogEntry(){
        List<LogExerciseSet> exercises = new ArrayList<>();
        for (int i = 0; i < this.groups.size(); i++){
            ExerciseSetGroup group = this.groups.get(i);
            List<Double> weights = new ArrayList<>();
            List<Integer> reps = new ArrayList<>();
            for (int j = 0; j < group.getSetCount(); j++){
                if (isSetCompleted(i, j)){
                    weights.add(group.getWeight(j));
                    reps.add(group.getReps(j));
                }
            }
            if (!weights.isEmpty()){
                exercises.add(new LogExerciseSet(group.getExercise().getName(), weights, reps));
            }
        }
        return new LogEntry(this.templateName, this.startTime, exercises);
    }
}
