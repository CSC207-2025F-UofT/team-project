package data_access;

import entities.User;
import use_case.signup.SignupDataAccessInterface;

import java.util.HashMap;
import java.util.Map;

/**
 * In-memory implementation of the DAO for storing user data.
 * This implementation uses HashMaps to store users by username and email.
 */
public class InMemoryUserDataAccessObject implements SignupDataAccessInterface {

    private final Map<String, User> usersByUsername = new HashMap<>();
    private final Map<String, User> usersByEmail = new HashMap<>();

    @Override
    public boolean existsByUsername(String username) {
        return usersByUsername.containsKey(username);
    }

    @Override
    public boolean existsByEmail(String email) {
        return usersByEmail.containsKey(email);
    }

    @Override
    public void save(User user) {
        usersByUsername.put(user.getUsername(), user);
        usersByEmail.put(user.getEmail(), user);
    }

    /**
     * Gets a user by username.
     * @param username the username to search for
     * @return the user with the given username, or null if not found
     */
    public User getUserByUsername(String username) {
        return usersByUsername.get(username);
    }

    /**
     * Gets a user by email.
     * @param email the email to search for
     * @return the user with the given email, or null if not found
     */
    public User getUserByEmail(String email) {
        return usersByEmail.get(email);
    }

    /**
     * Gets all users.
     * @return a map of all users by username
     */
    public Map<String, User> getAllUsers() {
        return new HashMap<>(usersByUsername);
    }

    /**
     * Clears all users from storage.
     * Useful for testing.
     */
    public void clear() {
        usersByUsername.clear();
        usersByEmail.clear();
import use_case.edit_profile.EditProfileDataAccessInterface;
import entities.User;

/**
 * In-memory implementation of user data access.
 * This is a placeholder until the actual data persistence layer is implemented.
 */
public class InMemoryUserDataAccessObject implements EditProfileDataAccessInterface {
    private final Map<String, User> users = new HashMap<>();

    @Override
    public void updateUserProfile(String username, String fullName, String bio, String profilePicture) {
        User user = users.get(username);
        if (user != null) {
            user.editProfile(fullName, bio, profilePicture);
        }
    }
}
