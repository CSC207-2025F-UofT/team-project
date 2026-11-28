package entity;

import entity.User.Role;
/**
 * Factory for creating CommonUser objects.
 */
public class UserFactory {

    public User create(String name, String id, String email, String passwordHash, Role role, AdopterPreferences preference) {
        return new User(name, id, email, passwordHash, role, preference);
    }
}
