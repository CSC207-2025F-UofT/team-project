package api.OpenWeatherApi;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;

public class OpenWeatherApiDataFetcher implements WeatherDataFetcher {
    private final OkHttpClient client = new OkHttpClient();

    //loading token from env variables
    public static String getAPIToken() {
        return System.getenv("token");
    }

    @Override
    public JSONObject getWeather(HashMap<String, Double> coordinates) throws CityNotFoundException{
        String apiKey = getAPIToken();
        // Checks if there is an API key in env variables
        if (apiKey == null || apiKey.isEmpty()) {
            throw new RuntimeException("API token not set in environment variable");
        }

        final Request request = new Request.Builder()
                .url("https://pro.openweathermap.org/data/2.5/forecast/hourly?lat="
                        + coordinates.get("lat") + "&lon=" + coordinates.get("lon") + "&appid=" + apiKey).build();
        try (Response response = client.newCall(request).execute()) {
            final JSONObject responseBody = new JSONObject(response.body().string());
            if (responseBody.length() > 2) {
                return responseBody;
            }
            else{
                throw new CityNotFoundException("New response in OpenWeatherApiDataFetcher");
            }
        } catch (IOException e) {
            throw new CityNotFoundException("IO EXCEPTION IN OpenWeatherApiDataFetcher");
        }

    }
}
