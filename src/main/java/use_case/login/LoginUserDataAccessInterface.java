package use_case.login;

import Entity.User;

/**
 * DAO interface for the Login Use Case.
 */
public interface LoginUserDataAccessInterface {

    /**
     * Checks if a user with the given email exists.
     * @param email the email to look for
     * @return true if a user with the given email exists; false otherwise
     */
    boolean existsByEmail(String email);

    /**
     * Returns the user with the given email.
     * @param email the email to look up
     * @return the user with the given email
     */
    User getByEmail(String email);

    /**
     * Records which user is currently logged in.
     * @param id the id of the logged-in user
     */
    void setCurrentUserId(String id);
}
