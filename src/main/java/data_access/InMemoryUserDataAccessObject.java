package data_access;

import entity.User;

import java.util.HashMap;
import java.util.Map;

/**
 * Stores users in memory only (lost when the app closes). Used for tests.
 */
public class InMemoryUserDataAccessObject implements UserDataAccessObject {
    private final Map<String, User> users = new HashMap<>();
    private String currentUsername = null;

    @Override
    public boolean existsByName(String username) {
        return users.containsKey(username);
    }

    @Override
    public void save(User user) {
        users.put(user.getUsername(), user);
    }

    @Override
    public User get(String username) {
        return users.get(username);
    }

    @Override
    public String getCurrentUsername() {
        return currentUsername;
    }

    @Override
    public void setCurrentUsername(String username) {
        this.currentUsername = username;
    }
}
