package use_case.update_profile;

import entity.User;

/**
 * User data access interface for the Update Profile use case.
 * Provides only the methods the interactor is allowed to use.
 */
public interface UpdateUserProfileUserDataAccessInterface {

    /**
     * Retrieves the currently logged-in user.
     *
     * @return the current User object, or null if no user is logged in
     */
    User getCurrentUser();

    /**
     * Saves the updated user information to the data store.
     *
     * @param user the updated User object to persist
     */
    void save(User user);
}
