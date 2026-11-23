package data_access;

import entity.DailyWeather;
import entity.WeatherCondition;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class WeatherDataAccessObjectTest {
    private static final LocalDate DATE = LocalDate.of(2025, 9, 3);

    @Test
    void parsesOpenMeteoDailyResponse() {
        JSONObject response = new JSONObject("{\"daily\":{\"time\":[\"2025-09-03\"],"
                + "\"weather_code\":[73],\"temperature_2m_max\":[-1.5],\"temperature_2m_min\":[-6.0]}}");

        DailyWeather weather = OpenMeteoWeatherDataAccessObject.parseDay(response, DATE);

        assertEquals(DATE, weather.getDate());
        assertEquals(WeatherCondition.SNOW, weather.getCondition());
        assertEquals(-1.5, weather.getHighTemperature());
        assertEquals(-6.0, weather.getLowTemperature());
    }

    @Test
    void bundledDataProvidesTermDates() {
        DailyWeather weather = new BundledWeatherDataAccessObject().getWeather(DATE);

        assertEquals(WeatherCondition.RAIN, weather.getCondition());
        assertEquals(25.1, weather.getHighTemperature());
        assertEquals(16.1, weather.getLowTemperature());
    }

    @Test
    void fallsBackToBundledDataWhenApiIsUnavailable() {
        OpenMeteoWeatherDataAccessObject dao = new OpenMeteoWeatherDataAccessObject(
                "http://127.0.0.1:1/unreachable", new BundledWeatherDataAccessObject());

        assertEquals(WeatherCondition.RAIN, dao.getWeather(DATE).getCondition());
    }
}
