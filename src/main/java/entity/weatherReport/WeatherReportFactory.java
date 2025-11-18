package entity.weatherReport;

import api.OpenWeatherApi.*;
import api.geocodingapi.CoordinatesFetcher;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.HashMap;

public class WeatherReportFactory {

    private final WeatherDataFetcher fetcher;
    private final CoordinatesFetcher fetcher1;
    public WeatherReportFactory(WeatherDataFetcher fetcher, CoordinatesFetcher fetcher1) {
        this.fetcher = fetcher;
        this.fetcher1 = fetcher1;
    }
    /**
     * Create a Weather report object given a location.
     */
    public WeatherReport create(String name) throws CoordinatesFetcher.CityNotFoundException, WeatherDataFetcher.CityNotFoundException {
        HashMap<String, Double> coordinates = fetcher1.getCoordinates(name);
        coordinates.put("lat", coordinates.get("lat"));
        coordinates.put("lon", coordinates.get("lon"));

        JSONObject weatherData = fetcher.getWeather(coordinates);

        String temperature = weatherData.getString("temperature");
        String humidity = weatherData.getString("humidity");
        String feelsLike = weatherData.getString("feelsLike");
        JSONArray weatherArray = weatherData.getJSONArray("weather");
        JSONObject currentWeather = weatherArray.getJSONObject(0);
        String weather = currentWeather.getString("main");

        /**
         * {
         *   "coord": {
         *     "lon": 0,
         *     "lat": 0
         *   },
         *   "weather": [
         *     {
         *       "id": 802,
         *       "main": "Clouds",
         *       "description": "scattered clouds",
         *       "icon": "03d"
         *     }
         *   ],
         *   "base": "stations",
         *   "main": {
         *     "temp": 298.84,
         *     "feels_like": 299.63,
         *     "temp_min": 298.84,
         *     "temp_max": 298.84,
         *     "pressure": 1011,
         *     "humidity": 83,
         *     "sea_level": 1011,
         *     "grnd_level": 1011
         *   },
         *   "visibility": 10000,
         *   "wind": {
         *     "speed": 6.05,
         *     "deg": 158,
         *     "gust": 6.12
         *   },
         *   "clouds": {
         *     "all": 26
         *   },
         *   "dt": 1763390046,
         *   "sys": {
         *     "sunrise": 1763358096,
         *     "sunset": 1763401718
         *   },
         *   "timezone": 0,
         *   "id": 6295630,
         *   "name": "Globe",
         *   "cod": 200
         * }
         */

        return new WeatherReport(name, weather, temperature, feelsLike, humidity);
    }
}
