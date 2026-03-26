package use_case.deleteFavouriteLocation;

import entity.user.User;

/**
 * DAO interface for the Delete Favourite Location Use Case.
 */
public interface DeleteLocationUserDataAccessInterface {

    void save(User user);

    User get(String username);

    String getCurrentUsername();

}
