package use_case.SearchLocation;

import entity.location.Location;

/**
 * interface for the search use case
 */

public interface SearchUserDataAccessInterface {
    /**
     * Checks if the given city exists
     * @param cityName the city to look for
     * @return true if the city exists; false otherwise
     */
    boolean existsByName(String cityName);
    Location get(String cityName);
    void setCurrentCityName(String cityName);
    String getCurrentCityName();
}
