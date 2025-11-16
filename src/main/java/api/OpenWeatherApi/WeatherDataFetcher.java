package api.OpenWeatherApi;


import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;

/**
 *  Interface for fetching weather data from given coordinates
 */

public interface WeatherDataFetcher {
    // Define the method used to interact with the database.
    // Get weather(latitude, longitude)
    JSONObject getWeather(HashMap<String, Double> coordinates) throws CityNotFoundException, IOException;
    class CityNotFoundException extends Exception {
        public CityNotFoundException(HashMap<String, Double> coordinates) {
            super("City not found: ");
        }
    }
}