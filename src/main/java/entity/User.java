package entity;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * A user of the app, with their workout plans and the workouts they have done.
 * A new user starts with no plans and no workout history.
 */
public class User {
    private final UUID userID;
    private final String username; // unique, enforced by the data-access layer
    private String displayName;
    private String password;
    private final List<WorkoutPlan> workoutPlans = new ArrayList<>();
    private final List<Workout> workoutHistory = new ArrayList<>();

    /**
     * Creates a user with no workout plans and no workout history.
     *
     * @param userID      the unique ID of the user
     * @param username    the name used to log in; must be unique, which the data-access layer checks
     * @param displayName the name shown to other users
     * @param password    the user's password
     */
    public User(UUID userID, String username, String displayName, String password) {
        this.userID = userID;
        this.username = username;
        this.displayName = displayName;
        this.password = password;
    }

    public UUID getUserID() { return userID; }
    public String getUsername() { return username; }
    public String getDisplayName() { return displayName; }
    public void setDisplayName(String displayName) { this.displayName = displayName; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public List<WorkoutPlan> getWorkoutPlans() { return workoutPlans; }
    public List<Workout> getWorkoutHistory() { return workoutHistory; }
}
