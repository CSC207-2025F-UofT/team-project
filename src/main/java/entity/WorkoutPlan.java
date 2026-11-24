package entity;

import java.util.List;
import java.util.UUID;

/**
 * A user's plan for a workout: an ordered list of exercises with their
 * targets. A plan can be a preset offered by the app, and it can be copied
 * from another user's plan.
 */
public class WorkoutPlan {
    private final UUID workoutPlanID;
    private String name;
    private final UUID userOwnerID;
    private final List<PlannedExercise> exercises; // ordered
    private boolean workoutCompleted;
    private final boolean isPreset;
    private final UUID copiedFromUserID; // optional, may be null

    /**
     * Creates a workout plan. The list of exercises is copied, so changing the caller's list
     * afterwards does not change the plan.
     *
     * @param workoutPlanID    the unique ID of the plan
     * @param name             the plan's name, such as "Push Day"
     * @param userOwnerID      the ID of the user who owns the plan
     * @param exercises        the exercises to do, in order
     * @param workoutCompleted whether the plan's workout has been completed
     * @param isPreset         whether the plan is a preset offered by the app
     * @param copiedFromUserID the ID of the user the plan was copied from, or {@code null} if it was not copied
     * @throws NullPointerException if the list of exercises is null or contains null
     */
    public WorkoutPlan(UUID workoutPlanID, String name, UUID userOwnerID, List<PlannedExercise> exercises,
                       boolean workoutCompleted, boolean isPreset, UUID copiedFromUserID) {
        this.workoutPlanID = workoutPlanID;
        this.name = name;
        this.userOwnerID = userOwnerID;
        this.exercises = List.copyOf(exercises);
        this.workoutCompleted = workoutCompleted;
        this.isPreset = isPreset;
        this.copiedFromUserID = copiedFromUserID;
    }

    public UUID getWorkoutPlanID() { return workoutPlanID; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public UUID getUserOwnerID() { return userOwnerID; }
    /** Returns the exercises in order; the list cannot be modified. */
    public List<PlannedExercise> getExercises() { return exercises; }
    public boolean isWorkoutCompleted() { return workoutCompleted; }
    public void setWorkoutCompleted(boolean workoutCompleted) { this.workoutCompleted = workoutCompleted; }
    public boolean isPreset() { return isPreset; }
    public UUID getCopiedFromUserID() { return copiedFromUserID; }
}
