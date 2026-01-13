package api.geocodingapi;

import java.util.HashMap;

/**
 * Interface for fetching coordinates (latitude and longitude) for a given city.
 */
public interface CoordinatesFetcher {

    /**
     * Fetch the longitude and latitude for the given city.
     *
     * @param cityName the name of the city to fetch coordinates for
     * @return HashMap with keys "lat" and "lon" and their corresponding values
     * @throws CityNotFoundException if the city does not exist
     */
    HashMap<String, Double> getCoordinates(String cityName) throws CityNotFoundException;

    /** A checked exception indicating that a given city does not exist. */
    class CityNotFoundException extends Exception {
        public CityNotFoundException(String cityName) {
            super("City not found: " + cityName);
        }

        public CityNotFoundException(String cityName, Throwable cause) {
            super("City not found: " + cityName, cause);
        }

        @SuppressWarnings("unused")
        public CityNotFoundException(String message, boolean passthrough) {
            super(message);
        }
    }
}
