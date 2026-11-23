package use_case.enter_name.welcomeMenu;

import entity.Player;

public interface ReturningPlayerDataAccessInterface {
    /**
     * Checks if the given username exists.
     * @param username the username to look for
     * @return the player stored at username if the username exists
     */
    Player get(String username);

    String getCurrentUsername();
}
