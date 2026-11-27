package use_case.signup;

import entity.playerData;

/**
 * What the Sign Up use case needs from storage.
 */
public interface SignupDataAccessInterface {

    /**
     * Checks whether an account with this username already exists.
     *
     * @param username the username to look for
     * @return true if the username is taken
     */
    boolean existsById(String username);

    /**
     * Saves a player account.
     *
     * @param player the account to save
     */
    void save(playerData player);
}
