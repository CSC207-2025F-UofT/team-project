package use_case.makeNewPlayer;

import entity.Player;

public interface NewPlayerDataAccessInterface {
    /**
     * Checks if the given username exists.
     * @param username the username to look for
     * @return true if a user with the given username exists; false otherwise
     */
    boolean existsByName(String username);

    /**
     * Saves the user.
     * @param player the player to save
     */
    void save(Player player);
}
