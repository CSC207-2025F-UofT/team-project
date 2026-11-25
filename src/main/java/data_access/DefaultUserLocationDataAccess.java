package data_access;

import entity.Location;
import use_case.find_nearby_quests.UserLocationDataAccessInterface;

/**
 * Gives a fixed user location: U of T St. George campus. This is the MVP's
 * stand-in for real location detection.
 */
public class DefaultUserLocationDataAccess implements UserLocationDataAccessInterface {
    private static final double UOFT_ST_GEORGE_LATITUDE = 43.6629;
    private static final double UOFT_ST_GEORGE_LONGITUDE = -79.3957;

    private static final Location DEFAULT_LOCATION =
            new Location(UOFT_ST_GEORGE_LATITUDE, UOFT_ST_GEORGE_LONGITUDE);

    @Override
    public Location getUserLocation() {
        return DEFAULT_LOCATION;
    }
}
