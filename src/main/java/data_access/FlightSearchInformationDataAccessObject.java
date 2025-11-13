package data_access;

import use_case.find_flight.LogSearchInfoDataAccessInterface;
import entity.FlightSearchInformation;

/**
 * Data Access Object for flight search information
 */

public class FlightSearchInformationDataAccessObject implements LogSearchInfoDataAccessInterface {

    public FlightSearchInformationDataAccessObject() {}

    @Override
    public void log(FlightSearchInformation flightSearchInformation) {}
}
