package api.geocodingapi;

import java.util.*;

/**
 * This CoordinateFetcher caches fetch request results to improve performance and
 * lessen the load on the underlying data source. An implementation of CoordinatesFetcher
 * must be provided. The number of calls to the underlying fetcher are recorded.
 *
 * If a call produces a CityNotFoundException, then it is NOT cached
 * in this implementation. The provided tests check for this behaviour.
 *
 * The cache maps the name of a city to its coordinates (longitude and latitude).
 */
public class CachingCoordinatesFetcher implements CoordinatesFetcher {

    private final CoordinatesFetcher fetcher;
    private final Map<String, HashMap<String, Double>> cache = new HashMap<>();
    private int callsMade = 0;

    public CachingCoordinatesFetcher(CoordinatesFetcher fetcher) {
        this.fetcher = fetcher;
    }

    @Override
    public HashMap<String, Double> getCoordinates(String cityName) throws CityNotFoundException {
        String key = cityName.toLowerCase();

        // Return cached result if available
        if (cache.containsKey(key)) {
            return cache.get(key);
        }

        try {
            HashMap<String, Double> coordinates = fetcher.getCoordinates(cityName);
            cache.put(key, coordinates);
            callsMade++;
            return coordinates;
        } catch (CityNotFoundException e) {
            callsMade++;
            throw e;
        }
    }

    public int getCallsMade() {
        return callsMade;
    }
}
