package use_case.add_favourite_location;

import api.geocodingapi.CoordinatesFetcher;
import api.openWeatherApi.WeatherDataFetcher;
import data_access.InMemoryUserDataAccessObject;
import data_access.UserDataAccessInterface;
import entity.hourly_forecast_report.HourlyForecastReport;
import entity.user.User;
import entity.user.UserFactory;
import entity.weatherReport.WeatherReport;
import interface_adapter.weatherReportPage.WeatherReportPageState;
import org.junit.jupiter.api.Test;
import use_case.currentWeather.*;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class AddFavouriteLocationTest {

    @Test
    void addToFavouritesSuccess() throws CoordinatesFetcher.CityNotFoundException, WeatherDataFetcher.CityNotFoundException {
        CurrentWeatherInputData inputData = new CurrentWeatherInputData("Toronto");
        UserDataAccessInterface userRepository = new InMemoryUserDataAccessObject();
        UserFactory factory = new UserFactory();
        User user = factory.create("username", "password", new ArrayList<>());
        userRepository.save(user);
        userRepository.setCurrentUsername(user.getName());

        CurrentWeatherOutputBoundary presenter = new CurrentWeatherOutputBoundary() {
            @Override
            public void addToFavouriteSuccess(CurrentWeatherOutputData outputData) {
                assertEquals("Toronto", outputData.getCityName());
            }

            @Override
            public void addToFavouriteFail(CurrentWeatherOutputData outputData) {
                fail("Use case failure is unexpected.");
            }

            @Override
            public void switchToFavouritesPageView(List<WeatherReport> weatherReports) {}

            @Override
            public void switchToHourlyForecast(HourlyForecastReport report, String cityName) {

            }

            @Override
            public void switchToLoggedInSearchView() {}
            @Override
            public void switchToLoggedInHomePageView() {}
            @Override
            public void switchToCheckOutfitView(WeatherReportPageState state) {}
            @Override
            public void resetPopUpMessage() {}
        };
        CurrentWeatherInputBoundary interactor = new CurrentWeatherInteractor(userRepository, presenter);
        interactor.addToFavourites(inputData);
    }

    @Test
    void addToFavouriteFailure() throws CoordinatesFetcher.CityNotFoundException, WeatherDataFetcher.CityNotFoundException {
        CurrentWeatherInputData inputData = new CurrentWeatherInputData("Toronto");
        UserDataAccessInterface userRepository = new InMemoryUserDataAccessObject();
        UserFactory factory = new UserFactory();
        User user = factory.create("username", "password", new ArrayList<>());
        userRepository.save(user);
        userRepository.setCurrentUsername(user.getName());
        userRepository.addLocation("Toronto");

        CurrentWeatherOutputBoundary presenter = new CurrentWeatherOutputBoundary() {
            @Override
            public void addToFavouriteSuccess(CurrentWeatherOutputData outputData) {
                fail("Use case failure is unexpected.");
            }

            @Override
            public void addToFavouriteFail(CurrentWeatherOutputData outputData) {
                assertEquals("Toronto", outputData.getCityName());
            }

            @Override
            public void switchToFavouritesPageView(List<WeatherReport> weatherReports) {}
            @Override
            public void switchToLoggedInSearchView() {}
            @Override
            public void switchToLoggedInHomePageView() {}
            @Override
            public void switchToCheckOutfitView(WeatherReportPageState state) {}
            @Override
            public void resetPopUpMessage() {}
            @Override
            public void switchToHourlyForecast(HourlyForecastReport report, String cityName) {}
        };
        CurrentWeatherInputBoundary interactor = new CurrentWeatherInteractor(userRepository, presenter);
        interactor.addToFavourites(inputData);
    }
}
