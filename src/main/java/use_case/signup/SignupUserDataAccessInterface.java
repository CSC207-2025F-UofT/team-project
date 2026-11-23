package use_case.signup;

import entity.User;

/**
 * DAO interface for the (adopter) Signup Use Case.
 */
public interface SignupUserDataAccessInterface {

    /**
     * Checks if a user with the given id exists.
     * @param id the id to look for
     * @return true if a user with the given id exists; false otherwise
     */
    boolean existsById(String id);

    /**
     * Checks if a user with the given email exists.
     * @param email the email to look for
     * @return true if a user with the given email exists; false otherwise
     */
    boolean existsByEmail(String email);

    /**
     * Saves the user.
     * @param user the user to save
     */
    void save(User user);
}
