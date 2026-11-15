package entity;

/**
 * Factory for creating User objects.
 */

public class UserFactory {
    /**
     * Creates a new User.
     * @param name the username
     * @param password the password
     * @return a new User with the given name and password
     * @throws IllegalArgumentException if the name or password is empty
     */
    public User create(String name, String password){
        return new User(name, password);
    }
}
