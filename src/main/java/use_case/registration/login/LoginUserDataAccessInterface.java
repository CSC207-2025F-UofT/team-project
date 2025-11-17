package use_case.registration.login;

import entity.User;
import use_case.DataAccessException;

/**
 * DAO interface for the Login Use Case.
 */
public interface LoginUserDataAccessInterface {

    /**
     * Checks if the given username exists.
     * @param username the username to look for
     * @return true if a user with the given username exists; false otherwise
     * @throws DataAccessException if there is an error accessing data
     */
    boolean existsByName(String username) throws DataAccessException;

    /**
     * Saves the user.
     * @param user the user to save
     * @throws DataAccessException if there is an error accessing data
     */
    void save(User user) throws DataAccessException;

    /**
     * Returns the user with the given username.
     * @param username the username to look up
     * @return the user with the given username
     * @throws DataAccessException if there is an error accessing data
     */
    User get(String username) throws DataAccessException;

    /**
     * Sets the currently logged-in username.
     */
    void setCurrentUsername(String name);

    /**
     * Returns the currently logged-in username.
     */
    String getCurrentUsername();
}
