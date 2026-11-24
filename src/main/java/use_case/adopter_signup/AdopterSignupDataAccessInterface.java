package use_case.adopter_signup;

import entity.Adopter;

/**
 * What the signup use case needs from storage.
 */
public interface AdopterSignupDataAccessInterface {
    boolean existsByUsername(String username);

    void save(Adopter adopter);
}
