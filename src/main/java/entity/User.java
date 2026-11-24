package entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.regex.Pattern;

/**
 * A user of the app, with their workout plans and the workouts they have done.
 * A new user starts with no plans and no workout history.
 */
public class User {
    private static final Pattern USERNAME = Pattern.compile("[a-z0-9_]{3,24}");
    private static final Pattern BLANK = Pattern.compile("[\\s\\p{javaWhitespace}]*", Pattern.UNICODE_CHARACTER_CLASS);

    private final UUID userID;
    private final String username;
    private String displayName;
    private final List<WorkoutPlan> workoutPlans = new ArrayList<>();
    private final List<Workout> workoutHistory = new ArrayList<>();

    /**
     * Creates a user with no workout plans and no workout history.
     *
     * @param userID      the unique ID of the user
     * @param username    the name used to log in; must be unique, which the data-access layer checks
     * @param displayName the name shown to other users
     * @throws IllegalArgumentException if the username or display name breaks the rules checked by
     *     {@link #isValidUsername} and {@link #isValidDisplayName}
     */
    public User(UUID userID, String username, String displayName) {
        if (!isValidUsername(username)) {
            throw new IllegalArgumentException("Invalid username.");
        }
        this.userID = Objects.requireNonNull(userID);
        this.username = username;
        setDisplayName(displayName);
    }

    /** Usernames are 3 to 24 lowercase ASCII letters, digits or underscores, and are unique across users. */
    public static boolean isValidUsername(String username) {
        return username != null && USERNAME.matcher(username).matches();
    }

    /** A display name must contain at least one non-whitespace character. */
    public static boolean isValidDisplayName(String displayName) {
        return displayName != null && !BLANK.matcher(displayName).matches();
    }

    public UUID getUserID() { return userID; }
    public String getUsername() { return username; }
    public String getDisplayName() { return displayName; }

    public void setDisplayName(String displayName) {
        if (!isValidDisplayName(displayName)) {
            throw new IllegalArgumentException("Invalid display name.");
        }
        this.displayName = displayName;
    }

    public List<WorkoutPlan> getWorkoutPlans() { return workoutPlans; }
    public List<Workout> getWorkoutHistory() { return workoutHistory; }
}
