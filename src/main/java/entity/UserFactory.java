package entity;

/**
 * Creates User objects. Interactors use this instead of calling the
 * constructor directly, so how users are built can change in one place.
 */
public class UserFactory {
    public User create(String username, String password) {
        return new User(username, password);
    }
}
