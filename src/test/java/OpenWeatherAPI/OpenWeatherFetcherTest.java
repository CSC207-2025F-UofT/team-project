package OpenWeatherAPI;

import api.OpenWeatherApi.OpenWeatherApiDataFetcher;
import api.OpenWeatherApi.WeatherDataFetcher;
import api.geocodingapi.GeocodingApiCoordinatesFetcher;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import api.geocodingapi.CoordinatesFetcher;

import java.util.HashMap;
import static org.junit.jupiter.api.Assertions.*;


public class OpenWeatherFetcherTest {

    @Test
    void testValidLocationReturnsResults() throws WeatherDataFetcher.CityNotFoundException{
        HashMap<String, Double> coordinates = new  HashMap<>();
        coordinates.put("lat", 0.0);
        coordinates.put("lon", 0.0);
        WeatherDataFetcher fetcher = new OpenWeatherApiDataFetcher();
        JSONObject weather = fetcher.getWeather(coordinates);
        String jsonString = """
                {
                  "coord": {
                    "lon": 0,
                    "lat": 0
                  },
                  "weather": [
                    {
                      "id": 804,
                      "main": "Clouds",
                      "description": "overcast clouds",
                      "icon": "04d"
                    }
                  ],
                  "base": "stations",
                  "main": {
                    "temp": 298.92,
                    "feels_like": 299.69,
                    "temp_min": 298.92,
                    "temp_max": 298.92,
                    "pressure": 1010,
                    "humidity": 82,
                    "sea_level": 1010,
                    "grnd_level": 1010
                  },
                  "visibility": 10000,
                  "wind": {
                    "speed": 6.26,
                    "deg": 175,
                    "gust": 6
                  },
                  "clouds": {
                    "all": 86
                  },
                  "dt": 1763478561,
                  "sys": {
                    "sunrise": 1763444509,
                    "sunset": 1763488131
                  },
                  "timezone": 0,
                  "id": 6295630,
                  "name": "Globe",
                  "cod": 200
                }""";
        assertEquals(JSONObject(expected).toString(), weather.toString());
    }
}
