package use_case.enter_name;

import entity.Player;
import use_case.shared.DataAccessException;

/** Player storage, as seen by Enter Name. */
public interface EnterNameDataAccessInterface {
    boolean playerExists(String name) throws DataAccessException;

    /** @return the saved player, or null if there is no player with that name */
    Player getPlayer(String name) throws DataAccessException;

    void savePlayer(Player player) throws DataAccessException;
}
