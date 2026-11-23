package entity;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class DailyWeatherTest {
    private static DailyWeather weather(WeatherCondition condition) {
        return new DailyWeather(LocalDate.of(2025, 9, 2), condition, 20.0, 10.0);
    }

    @Test
    void restorePercentDependsOnCondition() {
        assertEquals(100, weather(WeatherCondition.CLEAR).getEnergyRestorePercent());
        assertEquals(95, weather(WeatherCondition.CLOUDY).getEnergyRestorePercent());
        assertEquals(85, weather(WeatherCondition.RAIN).getEnergyRestorePercent());
        assertEquals(80, weather(WeatherCondition.SNOW).getEnergyRestorePercent());
    }

    @Test
    void wmoCodesMapToGameConditions() {
        assertEquals(WeatherCondition.CLEAR, WeatherCondition.fromWmoCode(0));
        assertEquals(WeatherCondition.CLEAR, WeatherCondition.fromWmoCode(1));
        assertEquals(WeatherCondition.CLOUDY, WeatherCondition.fromWmoCode(3));
        assertEquals(WeatherCondition.CLOUDY, WeatherCondition.fromWmoCode(45));
        assertEquals(WeatherCondition.RAIN, WeatherCondition.fromWmoCode(61));
        assertEquals(WeatherCondition.RAIN, WeatherCondition.fromWmoCode(95));
        assertEquals(WeatherCondition.SNOW, WeatherCondition.fromWmoCode(73));
        assertEquals(WeatherCondition.SNOW, WeatherCondition.fromWmoCode(85));
    }
}
