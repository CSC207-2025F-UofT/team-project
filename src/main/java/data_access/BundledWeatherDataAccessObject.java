package data_access;

import entity.DailyWeather;
import org.json.JSONObject;
import use_case.rest.RestWeatherDataAccessInterface;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;

/**
 * Reads a bundled copy of the Open-Meteo historical Toronto weather, used when the API is unavailable.
 */
public class BundledWeatherDataAccessObject implements RestWeatherDataAccessInterface {
    private static final String RESOURCE = "/weather/toronto-weather.json";

    private final JSONObject data;

    public BundledWeatherDataAccessObject() {
        try (InputStream in = getClass().getResourceAsStream(RESOURCE)) {
            if (in == null) {
                throw new IllegalStateException("Missing bundled weather data: " + RESOURCE);
            }
            data = new JSONObject(new String(in.readAllBytes(), StandardCharsets.UTF_8));
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    @Override
    public DailyWeather getWeather(LocalDate date) {
        return OpenMeteoWeatherDataAccessObject.parseDay(data, date);
    }
}
