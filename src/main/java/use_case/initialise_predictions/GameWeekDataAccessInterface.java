package use_case.initialise_predictions;

import java.io.IOException;

/**
 * Interface for accessing gameweek live data
 * Contains player stats for a specific gameweek
 */
public interface GameWeekDataAccessInterface {
    /**
     * Fetch live data for a specific gameweek
     * This is called MULTIPLE times (once per finished gameweek)
     * @param gameWeekId The gameweek number (1-38)
     * @return Raw JSON string of gameweek live data
     */
    String getGameWeekLiveData(int gameWeekId) throws IOException;
}