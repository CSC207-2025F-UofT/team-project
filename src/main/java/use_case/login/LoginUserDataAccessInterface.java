package use_case.login;

import entity.User;

/**
 * DAO interface for the Login Use Case.
 */
public interface LoginUserDataAccessInterface {

    /**
     * Returns the user with the given email.
     * @param email the email to look up
     * @return the user with the given email, or null if there is none
     */
    User getByEmail(String email);

    /**
     * Records which user is currently logged in.
     * @param id the id of the logged-in user
     */
    void setCurrentUserId(String id);
}
