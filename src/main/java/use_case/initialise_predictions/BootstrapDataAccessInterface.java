package use_case.initialise_predictions;

import java.io.IOException;

/**
 * Interface for accessing FPL bootstrap data
 * Bootstrap data contains: all players, teams, gameweeks, etc.
 */
public interface BootstrapDataAccessInterface {
    /**
     * Fetch bootstrap data from FPL API
     * This is called ONCE at program startup
     * @return Raw JSON string of bootstrap data
     */
    String getBootstrapData() throws IOException;
}