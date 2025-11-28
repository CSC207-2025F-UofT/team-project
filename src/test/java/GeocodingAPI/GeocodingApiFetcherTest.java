package GeocodingAPI;

import api.geocodingapi.CoordinatesFetcher;
import api.geocodingapi.GeocodingApiCoordinatesFetcher;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

public class GeocodingApiFetcherTest {

    @Test
    void testFetchCoordinatesValidCity() throws CoordinatesFetcher.CityNotFoundException {
        GeocodingApiCoordinatesFetcher fetcher = new GeocodingApiCoordinatesFetcher();
        HashMap<String, Double> coordinates = fetcher.getCoordinates("Toronto");

        HashMap<String, Double> expectedCoordinates = new HashMap<>();
        expectedCoordinates.put("lon", -79.3839347);
        expectedCoordinates.put("lat", 43.6534817);

        assertEquals(coordinates, expectedCoordinates);
    }

    @Test
    void testInvalidCityNameThrowsException() {
        GeocodingApiCoordinatesFetcher fetcher = new GeocodingApiCoordinatesFetcher();

        assertThrows(CoordinatesFetcher.CityNotFoundException.class, () -> fetcher.getCoordinates("XYZZ"));
    }
}