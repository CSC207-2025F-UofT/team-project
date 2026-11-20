package use_case.shared;

import entity.Player;

/** Who is playing right now. Used by Enter Name, View Stats and Finish Round. */
public interface CurrentPlayerDataAccessInterface {
    /** @return the current player, or null if no one has entered a name yet */
    Player getCurrentPlayer();

    void setCurrentPlayer(Player player);
}
