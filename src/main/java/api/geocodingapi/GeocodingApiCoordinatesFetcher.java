package api.geocodingapi;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;

public class GeocodingApiCoordinatesFetcher implements CoordinatesFetcher {

    private static final String ENDPOINT_TEMPLATE = "https://api.openweathermap.org/geo/1.0/direct?q=%s&limit=5&appid=%s";
    private final OkHttpClient client = new OkHttpClient();

    // load token from env variable.
    public static String getAPIToken() {
        return System.getenv("token");
    }

    @Override
    public HashMap<String, Double> getCoordinates(String cityName) throws CityNotFoundException {
        String apiKey = getAPIToken();
        // Checks if there is an API key in env variable
        if (apiKey == null || apiKey.isEmpty()) {
            throw new RuntimeException("API token not set in environment variable");
        }

        String url = String.format(ENDPOINT_TEMPLATE, cityName, apiKey);
        Request request = new Request.Builder().url(url).build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new CityNotFoundException(cityName, true);
            }

            assert response.body() != null;
            String body = response.body().string();
            JSONArray arr = new JSONArray(body);

            if (arr.length() == 0) {
                throw new CityNotFoundException(cityName);
            }

            JSONObject json = arr.getJSONObject(0);
            double lat = json.getDouble("lat");
            double lon = json.getDouble("lon");

            HashMap<String, Double> coordinates = new HashMap<>();
            coordinates.put("lat", lat);
            coordinates.put("lon", lon);
            return coordinates;

        } catch (IOException e) {
            throw new CityNotFoundException(cityName, e);
        }
    }
}