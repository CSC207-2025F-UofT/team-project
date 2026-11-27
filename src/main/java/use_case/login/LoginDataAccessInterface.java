package use_case.login;

import entity.playerData;

/**
 * What the Login use case needs from storage.
 */
public interface LoginDataAccessInterface {

    /**
     * Checks whether an account with this username exists.
     *
     * @param username the username to look for
     * @return true if the account exists
     */
    boolean existsById(String username);

    /**
     * Returns the account with this username.
     *
     * @param username the username to look up
     * @return the player's account, or null if there is none
     */
    playerData getPlayerData(String username);

    /**
     * Records who is logged in, so other use cases (like viewing stats) know.
     *
     * @param username the logged-in player's username, or null to log out
     */
    void setCurrentPlayerId(String username);
}
