package use_case.registration.signup;

import entity.User;
import use_case.DataAccessException;

/**
 * DAO interface for the Signup Use Case.
 */
public interface SignupUserDataAccessInterface {

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
}
