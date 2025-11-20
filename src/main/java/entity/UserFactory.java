package entity;

/**
 * Factory for creating CommonUser objects.
 */
public class UserFactory {

    public User create(String email, String name, String password) {
        return new User(email, name, password);
    }
}
