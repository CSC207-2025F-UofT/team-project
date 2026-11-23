package data_access;

import entity.DailyWeather;
import entity.WeatherCondition;
import org.json.JSONArray;
import org.json.JSONObject;
import use_case.rest.RestWeatherDataAccessInterface;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDate;

/**
 * Fetches historical Toronto weather from the Open-Meteo Historical Weather API (no API key needed).
 * Falls back to another source when the API cannot be reached or returns unusable data.
 */
public class OpenMeteoWeatherDataAccessObject implements RestWeatherDataAccessInterface {
    public static final String DEFAULT_BASE_URL = "https://archive-api.open-meteo.com/v1/archive";
    private static final String QUERY = "?latitude=43.6629&longitude=-79.3957"
            + "&daily=weather_code,temperature_2m_max,temperature_2m_min&timezone=America%%2FToronto"
            + "&start_date=%s&end_date=%s";

    private final String baseUrl;
    private final RestWeatherDataAccessInterface fallback;
    private final HttpClient client = HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(5)).build();

    public OpenMeteoWeatherDataAccessObject(String baseUrl, RestWeatherDataAccessInterface fallback) {
        this.baseUrl = baseUrl;
        this.fallback = fallback;
    }

    @Override
    public DailyWeather getWeather(LocalDate date) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(baseUrl + String.format(QUERY, date, date)))
                    .timeout(Duration.ofSeconds(10))
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() != 200) {
                return fallback.getWeather(date);
            }
            return parseDay(new JSONObject(response.body()), date);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return fallback.getWeather(date);
        } catch (Exception e) {
            return fallback.getWeather(date);
        }
    }

    /**
     * Extracts one day's weather from an Open-Meteo "daily" response.
     * @throws IllegalArgumentException if the response has no usable data for the date
     */
    static DailyWeather parseDay(JSONObject response, LocalDate date) {
        JSONObject daily = response.getJSONObject("daily");
        JSONArray times = daily.getJSONArray("time");
        for (int i = 0; i < times.length(); i++) {
            if (times.getString(i).equals(date.toString())) {
                if (daily.getJSONArray("weather_code").isNull(i)) {
                    break;
                }
                return new DailyWeather(date,
                        WeatherCondition.fromWmoCode(daily.getJSONArray("weather_code").getInt(i)),
                        daily.getJSONArray("temperature_2m_max").getDouble(i),
                        daily.getJSONArray("temperature_2m_min").getDouble(i));
            }
        }
        throw new IllegalArgumentException("No weather data for " + date);
    }
}
