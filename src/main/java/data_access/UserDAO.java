package data_access;

import entity.User;

/**
 * Data Access Object interface for User persistence
 */
public interface UserDAO {
    /**
     * Save user data to persistent storage
     * @param user User object to save
     */
    void saveUser(User user);

    /**
     * Load user data from persistent storage
     * @return User object, or null if not found
     */
    User loadUser();

    /**
     * Check if user data exists
     * @return true if user data exists, false otherwise
     */
    boolean userExists();
}