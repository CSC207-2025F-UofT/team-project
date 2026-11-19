package use_case.signup;

import entity.User;

/**
 * What the Signup use case needs from storage — nothing more (Interface Segregation).
 */
public interface SignupUserDataAccessInterface {
    boolean existsByName(String username);

    void save(User user);
}
