package entities;

/**
 * An entity representing a user's account.
 */
public abstract class User {
    private final String username;
    private final String role;

    /**
     * Creates a user with non-empty username.
     * @param username the username used to signup.
     */
    public User(String username, String role) {
        this.username = username;
        this.role = role;
    }

    public String getUsername() {
        return this.username;
    }
    public String getRole() { return this.role; }
}
