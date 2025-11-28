package use_case.loggedInSearchPage;

import api.OpenWeatherApi.OpenWeatherApiDataFetcher;
import api.OpenWeatherApi.WeatherDataFetcher;
import api.geocodingapi.CoordinatesFetcher;
import api.geocodingapi.GeocodingApiCoordinatesFetcher;
import api.googlePlacesAPI.GooglePlacesFetcher;
import api.googlePlacesAPI.PlaceFetcher;
import entity.placeSuggestions.PlaceSuggestion;
import entity.weatherReport.WeatherReport;
import entity.weatherReport.WeatherReportFactory;
import use_case.loggedInHomePage.LoggedInHomePageInputBoundary;
import use_case.loggedInHomePage.LoggedInHomePageOutputBoundary;

import java.util.HashMap;
import java.util.List;

public class LoggedInSearchPageInteractor implements LoggedInSearchPageInputBoundary {
    private final LoggedInSearchPageOutputBoundary userPresenter;
    private final GooglePlacesFetcher fetcher;

    public LoggedInSearchPageInteractor(LoggedInSearchPageOutputBoundary loggedInSearchPageOutputBoundary, GooglePlacesFetcher fetcher) {
        userPresenter = loggedInSearchPageOutputBoundary;
        this.fetcher = fetcher;
    }

    @Override
    public void switchToLoggedInHomePageView() {

        userPresenter.switchToLoggedInHomePageView();
    }

    @Override
    public void fetchSuggestions(String query) throws PlaceFetcher.PlaceNotFoundException {
        List<PlaceSuggestion> suggestions = fetcher.getPlace(query);
        userPresenter.showSuggestionsToUser(suggestions);
    }

    @Override
    public void execute(String query) {
        WeatherReport weatherReport;
        CoordinatesFetcher coordinatesFetcher = new GeocodingApiCoordinatesFetcher();
        WeatherDataFetcher fetcher = new OpenWeatherApiDataFetcher();
        WeatherReportFactory factory = new WeatherReportFactory(fetcher, coordinatesFetcher);
        try {
            weatherReport = factory.create(query);
        } catch (WeatherDataFetcher.CityNotFoundException | CoordinatesFetcher.CityNotFoundException e) {
            throw new RuntimeException(e);
        }
        userPresenter.switchToWeatherReportView(weatherReport);
    }
}