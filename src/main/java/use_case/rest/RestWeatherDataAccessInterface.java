package use_case.rest;

import entity.DailyWeather;

import java.time.LocalDate;

/**
 * Provides the historical Toronto weather for a real date.
 */
public interface RestWeatherDataAccessInterface {
    DailyWeather getWeather(LocalDate date);
}
