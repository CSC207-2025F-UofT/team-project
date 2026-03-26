package data_access;

import entity.user.User;
import use_case.createAccount.CreateAccountUserDataAccessInterface;
import use_case.deleteFavouriteLocation.DeleteLocationUserDataAccessInterface;
import use_case.signIn.SignInUserDataAccessInterface;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * In-memory implementation of the DAO for storing user data. This implementation does
 * NOT persist data between runs of the program.
 */
public class InMemoryUserDataAccessObject implements CreateAccountUserDataAccessInterface,
        SignInUserDataAccessInterface, DeleteLocationUserDataAccessInterface, UserDataAccessInterface {

    private final Map<String, User> users = new HashMap<>();

    private String currentUsername;

    @Override
    public boolean existsByName(String identifier) {
        return users.containsKey(identifier);
    }

    @Override
    public void save(User user) {
        users.put(user.getName(), user);
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
    public void setCurrentUsername(String name) {
        currentUsername = name;
    }

    @Override
    public void addLocation(String location) {
        if (currentUsername == null) {
            throw new IllegalStateException("No user is signed in.");
        }

        User user = users.get(currentUsername);
        if (user == null) {
            throw new RuntimeException("Current user not found.");
        }
        user.addLocation(location);

        users.put(currentUsername, user);
        save(user);
    }

    @Override
    public List<String> getLocations() {
        if (currentUsername == null) {
            throw new IllegalStateException("No user is signed in.");
        }

        User user = users.get(currentUsername);
        if (user == null) {
            throw new RuntimeException("Current user not found.");
        }

        // Return a copy to protect domain state
        return new ArrayList<>(user.getLocations());
    }
}