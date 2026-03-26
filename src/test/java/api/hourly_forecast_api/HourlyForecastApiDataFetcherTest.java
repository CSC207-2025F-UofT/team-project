package api.hourly_forecast_api;

import api.geocodingapi.CoordinatesFetcher;
import api.geocodingapi.GeocodingApiCoordinatesFetcher;
import api.googlePlacesAPI.GooglePlacesFetcher;
import api.googlePlacesAPI.PlaceFetcher;
import entity.hourly_forecast_report.HourlyForecastReport;
import entity.placeSuggestions.PlaceSuggestion;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class HourlyForecastApiDataFetcherTest {

    @Test
    void testGetPlace_validPlace() throws PlaceFetcher.PlaceNotFoundException, CoordinatesFetcher.CityNotFoundException {
        // Prepare dummy coordinates
        HashMap<String, Double> coordinates = new HashMap<>();
        coordinates.put("lat", 0.0);
        coordinates.put("lon", 0.0);

        // Call the API fetcher
        HourlyForecastApiDataFetcher forecastFetcher = new HourlyForecastApiDataFetcher();
        HourlyForecastReport report = forecastFetcher.getHourlyForecast(coordinates);

        // Assertions
        assertNotNull(report, "Report should not be null");
        assertNotNull(report.getDateTimes(), "Report date times should not be null");
        assertNotNull(report.getTemperature(), "Report temperatures should not be null");
        assertNotNull(report.getWeather(), "Report weather data should not be null");
        assertEquals(report.getDateTimes().size(), report.getTemperature().size(),
                "DateTimes and Temperature lists should be the same size");
        assertEquals(report.getDateTimes().size(), report.getWeather().size(),
                "DateTimes and Weather lists should be the same size");

        // Optionally, check that there is at least some hourly data
        assertFalse(report.getDateTimes().isEmpty(), "Report should contain hourly data");
    }
}