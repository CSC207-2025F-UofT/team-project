package use_case.signup;

import entity.User;

/**
 * DataAccessObject interface for the Signup Use Case.
 */

public interface SignupUserAccessInterface {

    /**
     * Checks if the given username exists.
     * @param username the username to look for
     * @return True if a user with the given username exists, and returns false otherwise
     */
    boolean existsByName(String username);

    /**
     * Saves the user.
     * @param user the user to save
     */
    void save(User user);
}
