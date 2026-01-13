package use_case.loggedInFavouritesPage;

import api.openWeatherApi.OpenWeatherApiDataFetcher;
import api.openWeatherApi.WeatherDataFetcher;
import api.geocodingapi.CoordinatesFetcher;
import api.geocodingapi.GeocodingApiCoordinatesFetcher;
import entity.weatherReport.WeatherReport;
import entity.weatherReport.WeatherReportFactory;

public class LoggedInFavouritesPageInteractor implements LoggedInFavouritesPageInputBoundary {
    private final LoggedInFavouritesPageOutputBoundary userPresenter;

    public LoggedInFavouritesPageInteractor(LoggedInFavouritesPageOutputBoundary userPresenter) {
        this.userPresenter = userPresenter;
    }

    @Override
    public void switchToLoggedInHomePageView() {
        userPresenter.switchToLoggedInHomePageView();
    }

    @Override
    public void execute(String location) {
        WeatherReport weatherReport;
        CoordinatesFetcher coordinatesFetcher = new GeocodingApiCoordinatesFetcher();
        WeatherDataFetcher fetcher = new OpenWeatherApiDataFetcher();
        WeatherReportFactory factory = new WeatherReportFactory(fetcher, coordinatesFetcher);
        try {
            weatherReport = factory.create(location);
        } catch (WeatherDataFetcher.CityNotFoundException | CoordinatesFetcher.CityNotFoundException e) {
            throw new RuntimeException(e);
        }
        userPresenter.switchToWeatherReportView(weatherReport);
    }
}