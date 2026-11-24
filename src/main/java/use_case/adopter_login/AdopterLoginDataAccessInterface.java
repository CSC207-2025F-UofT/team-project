package use_case.adopter_login;

import entity.Adopter;

/**
 * What the login use case needs from storage.
 */
public interface AdopterLoginDataAccessInterface {
    boolean existsByUsername(String username);

    /** Returns the adopter with this username. Only call after existsByUsername returns true. */
    Adopter get(String username);
}
