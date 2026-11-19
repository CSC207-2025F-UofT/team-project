package use_case.login;

import entity.User;

/**
 * What the Login use case needs from storage.
 */
public interface LoginUserDataAccessInterface {
    boolean existsByName(String username);

    User get(String username);

    void setCurrentUsername(String username);
}
