package use_case.WelcomeMenu;

import entity.Player;
import interface_adapter.WelcomeMenu.WelcomeState;

public interface ReturningPlayerDataAccessInterface {
    /**
     * Checks if the given username exists.
     * @param username the username to look for
     * @return the player stored at username if the username exists
     */
    Player get(String username);

    String getCurrentUsername();
}
