package entity;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public class Workout {
    private final UUID workoutID;
    private final UUID userOwnerID;
    private final UUID planID;
    private final String planName;
    private final LocalDate date;
    private final List<LoggedExercise> exercises;

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
