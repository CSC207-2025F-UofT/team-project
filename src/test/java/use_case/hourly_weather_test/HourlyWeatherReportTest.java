package use_case.hourly_weather_test;

import api.geocodingapi.CoordinatesFetcher;
import api.geocodingapi.GeocodingApiCoordinatesFetcher;
import api.hourly_forecast_api.HourlyForecastApiDataFetcher;
import data_access.InMemoryUserDataAccessObject;
import data_access.UserDataAccessInterface;
import entity.hourly_forecast_report.HourlyForecastReport;
import entity.weatherReport.WeatherReport;
import interface_adapter.weatherReportPage.WeatherReportPageState;
import org.junit.jupiter.api.Test;
import use_case.currentWeather.*;

import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class HourlyWeatherReportTest {

    @Test
    void HourlyWeatherReportTest() throws CoordinatesFetcher.CityNotFoundException {
        UserDataAccessInterface userRepository = new InMemoryUserDataAccessObject();

        String cityName = "Toronto";
        GeocodingApiCoordinatesFetcher coordinatesFetcher = new GeocodingApiCoordinatesFetcher();
        HashMap<String, Double> coordinates = coordinatesFetcher.getCoordinates(cityName);

        HourlyForecastApiDataFetcher hourlyForecastApiDataFetcher = new HourlyForecastApiDataFetcher();
        HourlyForecastReport hourlyForecastReport = hourlyForecastApiDataFetcher.getHourlyForecast(coordinates);

        CurrentWeatherOutputBoundary outputBoundary = new CurrentWeatherOutputBoundary() {
            @Override
            public void switchToHourlyForecast(HourlyForecastReport report, String cityName) {
                assertEquals("Toronto", cityName);
            }

            @Override
            public void switchToLoggedInSearchView() {}
            @Override
            public void switchToLoggedInHomePageView() {}
            @Override
            public void switchToCheckOutfitView(WeatherReportPageState state) {}
            @Override
            public void addToFavouriteSuccess(CurrentWeatherOutputData outputData) {}
            @Override
            public void addToFavouriteFail(CurrentWeatherOutputData outputData) {}
            @Override
            public void resetPopUpMessage() {}
            @Override
            public void switchToFavouritesPageView(List<WeatherReport> weatherReports) {}
        };

        CurrentWeatherInputBoundary interactor = new CurrentWeatherInteractor(userRepository, outputBoundary);
        interactor.switchToHourlyForecast(cityName);

    }
}
