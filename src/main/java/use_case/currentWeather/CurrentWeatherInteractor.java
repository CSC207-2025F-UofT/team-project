package use_case.currentWeather;

import api.OpenWeatherApi.OpenWeatherApiDataFetcher;
import api.OpenWeatherApi.WeatherDataFetcher;
import api.geocodingapi.CoordinatesFetcher;
import api.geocodingapi.GeocodingApiCoordinatesFetcher;
import data_access.UserDataAccessInterface;
import entity.weatherReport.WeatherReport;
import entity.weatherReport.WeatherReportFactory;

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
}
