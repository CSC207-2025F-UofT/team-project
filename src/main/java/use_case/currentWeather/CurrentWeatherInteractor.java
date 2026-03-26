package use_case.currentWeather;

import api.geocodingapi.GeocodingApiCoordinatesFetcher;
import api.hourly_forecast_api.HourlyForecastApiDataFetcher;
import api.hourly_forecast_api.HourlyForecastFetcher;
import api.openWeatherApi.OpenWeatherApiDataFetcher;
import api.openWeatherApi.WeatherDataFetcher;
import api.geocodingapi.CoordinatesFetcher;
import data_access.UserDataAccessInterface;
import entity.hourly_forecast_report.HourlyForecastReport;
import entity.weatherReport.WeatherReport;
import entity.weatherReport.WeatherReportFactory;
import interface_adapter.weatherReportPage.WeatherReportPageState;
import interface_adapter.weatherReportPage.WeatherReportPageViewModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CurrentWeatherInteractor implements CurrentWeatherInputBoundary{
    private final UserDataAccessInterface userDataAccessInterface;
    private final CurrentWeatherOutputBoundary userPresenter;

    public CurrentWeatherInteractor(UserDataAccessInterface userDataAccessInterface,
                                    CurrentWeatherOutputBoundary currentWeatherOutputBoundary) {
        this.userDataAccessInterface = userDataAccessInterface;
        this.userPresenter = currentWeatherOutputBoundary;
    }

    @Override
    public void switchToLoggedInSearchView() {
        userPresenter.switchToLoggedInSearchView();
    }

    @Override
    public void switchToLoggedInHomePageView() {
        userPresenter.switchToLoggedInHomePageView();
    }

    @Override
    public void addToFavourites(CurrentWeatherInputData inputData) throws CoordinatesFetcher.CityNotFoundException, WeatherDataFetcher.CityNotFoundException {
        String cityName = inputData.getCityName();
        List<String> locations = userDataAccessInterface.getLocations();
        boolean alreadyExists = locations.contains(cityName);

        CurrentWeatherOutputData outputData = new CurrentWeatherOutputData(cityName);

        if (alreadyExists) {
            userPresenter.addToFavouriteFail(outputData);
        } else {
            userDataAccessInterface.addLocation(cityName);
            userPresenter.addToFavouriteSuccess(outputData);
        }
    }

    @Override
    public void resetPopUpMessage() {
        userPresenter.resetPopUpMessage();
    }

    @Override
    public void switchToFavouritesPageView() {
        // All favourite locations of the user
        List<String> locations = userDataAccessInterface.getLocations();
        List<WeatherReport> weatherReports = new ArrayList<>();
        CoordinatesFetcher coordinatesFetcher = new GeocodingApiCoordinatesFetcher();
        WeatherDataFetcher fetcher = new OpenWeatherApiDataFetcher();
        WeatherReportFactory factory = new WeatherReportFactory(fetcher, coordinatesFetcher);
        for (String location : locations) {
            try {
                WeatherReport weatherReport = factory.create(location);
                weatherReports.add(weatherReport);
            } catch (WeatherDataFetcher.CityNotFoundException |
                     CoordinatesFetcher.CityNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
        userPresenter.switchToFavouritesPageView(weatherReports);
    }

    @Override
    public void switchToHourlyForecast(String cityName) throws CoordinatesFetcher.CityNotFoundException {
        CoordinatesFetcher coordinatesFetcher = new GeocodingApiCoordinatesFetcher();
        HashMap<String, Double> coordinates = coordinatesFetcher.getCoordinates(cityName);
        HourlyForecastFetcher forecastFetcher = new HourlyForecastApiDataFetcher();
        HourlyForecastReport report = forecastFetcher.getHourlyForecast(coordinates);
        userPresenter.switchToHourlyForecast(report, cityName);
    }

    @Override
    public void switchToCheckOutfitView(WeatherReportPageState state) {
        userPresenter.switchToCheckOutfitView(state);
    }
}
