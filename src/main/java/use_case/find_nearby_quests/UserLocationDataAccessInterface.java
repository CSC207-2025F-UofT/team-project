package use_case.find_nearby_quests;

import entity.Location;

/** Where the user is. In the MVP this is a fixed default. */
public interface UserLocationDataAccessInterface {
    /**
     * Returns the user's current location.
     *
     * @return the location
     */
    Location getUserLocation();
}
