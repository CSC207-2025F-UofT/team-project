package entity;

import java.util.HashMap;
import java.util.Map;

/**
 * A simple entity representing a user. Users have username, email, encrypted
 * password, preferences and alert thresholds.
 */
public class User {

    private final String username;
    private final String passwordHash;
    private final UserPreferences preferences;
    private final Map<String, Float> alertThresholds;

    /**
     * Creates a new user with the given non-empty user id, username, encrypted
     * password, default preferences, and alert thresholds.
     *
     * @param username the username
     * @param passwordHash the encrypted password
     * @throws IllegalArgumentException if the name, user id, email, and
     * encrypted password are empty
     */
    public User(String username, String passwordHash) {
        if ("".equals(username) || "".equals(passwordHash)) {
            throw new IllegalArgumentException("Field(s) cannot be empty or null");
        }

        this.username = username;
        this.passwordHash = passwordHash;
        this.preferences = new UserPreferences();
        this.alertThresholds = new HashMap<>();
    }

    public boolean authenticatePassword(String otherPasswordHash) {
        return otherPasswordHash.equals(passwordHash);
    }

    public void addAlertThreshold(String category, float threshold) {
        alertThresholds.put(category, threshold);
    }

    public String getUsername() {
        return username;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public UserPreferences getPreferences() {
        return preferences;
    }

    public Map<String, Float> getAlertThresholds() {
        return alertThresholds;
    }

}
