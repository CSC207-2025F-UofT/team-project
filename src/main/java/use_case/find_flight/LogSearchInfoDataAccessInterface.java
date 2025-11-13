package use_case.find_flight;

import entity.FlightSearchInformation;

/**
 * Data Access Object interface for logging the search information.
 */

public interface LogSearchInfoDataAccessInterface {

    /**
     * Logs the search information
     */

    void log(FlightSearchInformation flightSearchInformation);
}
