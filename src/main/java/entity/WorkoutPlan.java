package entity;

import java.util.List;
import java.util.UUID;

public class WorkoutPlan {
    private final UUID workoutPlanID;
    private String name;
    private final UUID userOwnerID;
    private final List<PlannedExercise> exercises; // ordered
    private boolean workoutCompleted;
    private final boolean isPreset;
    private final UUID copiedFromUserID; // optional, may be null

    public WorkoutPlan(UUID workoutPlanID, String name, UUID userOwnerID, List<PlannedExercise> exercises,
                       boolean workoutCompleted, boolean isPreset, UUID copiedFromUserID) {
        this.workoutPlanID = workoutPlanID;
        this.name = name;
        this.userOwnerID = userOwnerID;
        this.exercises = exercises;
        this.workoutCompleted = workoutCompleted;
        this.isPreset = isPreset;
        this.copiedFromUserID = copiedFromUserID;
    }

    public UUID getWorkoutPlanID() { return workoutPlanID; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public UUID getUserOwnerID() { return userOwnerID; }
    public List<PlannedExercise> getExercises() { return exercises; }
    public boolean isWorkoutCompleted() { return workoutCompleted; }
    public void setWorkoutCompleted(boolean workoutCompleted) { this.workoutCompleted = workoutCompleted; }
    public boolean isPreset() { return isPreset; }
    public UUID getCopiedFromUserID() { return copiedFromUserID; }
}
