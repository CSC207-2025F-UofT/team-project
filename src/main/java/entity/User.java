package entity;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class User {
    private final UUID userID;
    private final String username; // unique, enforced by the data-access layer
    private String displayName;
    private String password;
    private final List<WorkoutPlan> workoutPlans = new ArrayList<>();
    private final List<Workout> workoutHistory = new ArrayList<>();

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
