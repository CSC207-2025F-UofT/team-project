package data_access;

import entity.user.User;
import use_case.createAccount.CreateAccountUserDataAccessInterface;
import use_case.deleteFavouriteLocation.DeleteLocationUserDataAccessInterface;
import use_case.signIn.SignInUserDataAccessInterface;

import java.util.HashMap;
import java.util.Map;

/**
 * In-memory implementation of the DAO for storing user data. This implementation does
 * NOT persist data between runs of the program.
 */
public class InMemoryUserDataAccessObject implements CreateAccountUserDataAccessInterface,
        SignInUserDataAccessInterface, DeleteLocationUserDataAccessInterface {

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
}