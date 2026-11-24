package entity;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

/**
 * A workout a user did on a given day, following one of their
 * {@link WorkoutPlan}s, with the exercises as they were actually done.
 * The plan's name is stored too, so the workout history still shows it
 * if the plan is later renamed.
 */
public class Workout {
    private final UUID workoutID;
    private final UUID userOwnerID;
    private final UUID planID;
    private final String planName;
    private final LocalDate date;
    private final List<LoggedExercise> exercises;

    /**
     * Creates a workout.
     *
     * @param workoutID   the unique ID of the workout
     * @param userOwnerID the ID of the user who did the workout
     * @param planID      the ID of the plan the workout followed
     * @param planName    the plan's name at the time of the workout
     * @param date        the day the workout was done
     * @param exercises   the exercises that were done, in order
     */
    public Workout(UUID workoutID, UUID userOwnerID, UUID planID, String planName,
                   LocalDate date, List<LoggedExercise> exercises) {
        this.workoutID = workoutID;
        this.userOwnerID = userOwnerID;
        this.planID = planID;
        this.planName = planName;
        this.date = date;
        this.exercises = exercises;
    }

    public UUID getWorkoutID() { return workoutID; }
    public UUID getUserOwnerID() { return userOwnerID; }
    public UUID getPlanID() { return planID; }
    public String getPlanName() { return planName; }
    public LocalDate getDate() { return date; }
    public List<LoggedExercise> getExercises() { return exercises; }
}
