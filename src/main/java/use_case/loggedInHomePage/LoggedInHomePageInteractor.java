package use_case.loggedInHomePage;

import api.OpenWeatherApi.OpenWeatherApiDataFetcher;
import api.OpenWeatherApi.WeatherDataFetcher;
import api.geocodingapi.CoordinatesFetcher;
import api.geocodingapi.GeocodingApiCoordinatesFetcher;
import data_access.UserDataAccessInterface;
import entity.weatherReport.WeatherReport;
import entity.weatherReport.WeatherReportFactory;
import use_case.homePage.HomePageOutputBoundary;

import java.util.ArrayList;
import java.util.List;

public class LoggedInHomePageInteractor implements LoggedInHomePageInputBoundary {
    private final UserDataAccessInterface userDataAccessInterface;
    private final LoggedInHomePageOutputBoundary userPresenter;

    public LoggedInHomePageInteractor(UserDataAccessInterface userDataAccessInterface, LoggedInHomePageOutputBoundary loggedInHomePageOutputBoundary) {
        this.userDataAccessInterface = userDataAccessInterface;
        this.userPresenter = loggedInHomePageOutputBoundary;
    }

    @Override
    public void switchToLoggedInSearchView() {
        userPresenter.switchToLoggedInSearchView();
    }

    @Override
    public void switchToLoggedInFavouritesView() {
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
        userPresenter.switchToLoggedInFavouritesView(weatherReports);
    }
}
