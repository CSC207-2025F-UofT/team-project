package entity.location;

import api.geocodingapi.*;

import java.util.HashMap;

public class LocationFactory {

    private final CoordinatesFetcher fetcher;

    public LocationFactory(CoordinatesFetcher fetcher) {
        this.fetcher = fetcher;
    }

    /**
     * Create a Location object given a city name.
     */
    public Location create(String name) throws CoordinatesFetcher.CityNotFoundException {
        HashMap<String, Double> coordinates = fetcher.getCoordinates(name);

        double longitude = coordinates.get("lon");
        double latitude = coordinates.get("lat");

        return new Location(name, longitude, latitude);
    }
}