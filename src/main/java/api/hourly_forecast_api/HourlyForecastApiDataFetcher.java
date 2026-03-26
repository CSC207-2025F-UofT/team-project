package api.hourly_forecast_api;

import api.googlePlacesAPI.PlaceFetcher;
import api.openWeatherApi.WeatherDataFetcher;
import entity.hourly_forecast_report.HourlyForecastFactory;
import entity.hourly_forecast_report.HourlyForecastReport;
import entity.placeSuggestions.PlaceSuggestion;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class HourlyForecastApiDataFetcher implements HourlyForecastFetcher {
    private final OkHttpClient client = new OkHttpClient();
    private final String API_URL = "https://pro.openweathermap.org/data/2.5/forecast/hourly?";

    public static String getAPIToken() {
        return System.getenv("token");
    }

    @Override
    public HourlyForecastReport getHourlyForecast(HashMap<String, Double> coordinates) {
        final String API_KEY = getAPIToken();
        if (API_KEY == null || API_KEY.isEmpty()) {
            throw new RuntimeException("Google Places API key not set in environment variable");
        }

        String lat = coordinates.get("lat").toString();
        String lon = coordinates.get("lon").toString();
        String units = "metric";
        String numResponses = "6";
        String url = String.format(API_URL + "lat=%s&lon=%s&appid=%s&units=%s&cnt=%s", lat, lon, API_KEY, units, numResponses);

        final Request request = new Request.Builder().url(url).build();

        try (final Response response = client.newCall(request).execute()) {
            final JSONObject responseBody = new JSONObject(response.body().string());
            if (response.isSuccessful()) {
                return parseResponse(responseBody);
            } else {
                throw new CityNotFoundException();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public HourlyForecastReport parseResponse(JSONObject response) {
        HourlyForecastFactory factory = new HourlyForecastFactory();

        List<String> dateTimes = new ArrayList<>();
        List<String> weather = new ArrayList<>();
        List<String> temperature = new ArrayList<>();
        List<String> precipitation = new ArrayList<>();

        JSONArray list = response.getJSONArray("list");

        for (int i = 0; i < list.length(); i++) {
            JSONObject hourData = list.getJSONObject(i);

            String unixTime = String.valueOf(hourData.getLong("dt"));
            dateTimes.add(parseTime(unixTime));

            // weather array → take the first element's "main"
            JSONArray weatherArray = hourData.getJSONArray("weather");
            weather.add(weatherArray.getJSONObject(0).getString("main"));

            // main.temp
            JSONObject main = hourData.getJSONObject("main");
            temperature.add(String.valueOf(main.getDouble("temp")));

            // pop → probability of precipitation
            precipitation.add(String.valueOf(hourData.getDouble("pop")));
        }

        // Use factory to create the report
        return factory.create(dateTimes, weather, temperature, precipitation);
    }

    public String parseTime(String timestamp) {
        try {
            long unixSeconds = Long.parseLong(timestamp); // convert String to long
            LocalDateTime dateTime = LocalDateTime.ofInstant(
                    Instant.ofEpochSecond(unixSeconds),
                    ZoneId.systemDefault() // converts to local time zone
            );
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm"); // only hour:minute
            return dateTime.format(formatter);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return timestamp; // fallback if parsing fails
        }
    }
}
