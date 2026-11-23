package use_case.view_stats;

import entity.Player;
import use_case.shared.DataAccessException;

/** Player storage, as seen by View Stats. */
public interface ViewStatsDataAccessInterface {
    /** @return the saved player, or null if there is no player with that name */
    Player get(String name) throws DataAccessException;
}
