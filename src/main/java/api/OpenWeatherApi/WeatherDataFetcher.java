package api.OpenWeatherApi;


import org.json.JSONObject;

import java.util.HashMap;

/**
 *  Interface for fetching weather data from given coordinates
 */

public interface WeatherDataFetcher {
    // Define the method used to interact with the database.
    // Get weather(latitude, longitude)
    JSONObject getWeather(HashMap<String, Double> coordinates) throws CityNotFoundException;
    class CityNotFoundException extends Exception {
        public CityNotFoundException(String coordinates) {
            super("City not found: ");
        }
    }
}