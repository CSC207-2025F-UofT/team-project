package entities;

/**
 * An entity representing a user's account.
 */
abstract class User {
    private final String username;


    /**
     * Creates a user with non-empty username.
     * @param username the username used to login.
     */
    public User(String username) {
        this.username = username;
    }

    public String getUsername() {
        return this.username;
    }
}
