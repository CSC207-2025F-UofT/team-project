package entity;

/**
 * A user of the time-tracking app.
 * Other entities (Activity, TimeEntry) refer to their owner by username.
 */
public class User {
    private final String username;
    private final String password;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
