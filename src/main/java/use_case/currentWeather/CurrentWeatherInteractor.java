package use_case.currentWeather;

import api.openWeatherApi.WeatherDataFetcher;
import api.geocodingapi.CoordinatesFetcher;
import data_access.UserDataAccessInterface;
import entity.weatherReport.WeatherReport;
import entity.weatherReport.WeatherReportFactory;
import interface_adapter.weatherReportPage.WeatherReportPageState;

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
    public void switchToCheckOutfitView(WeatherReportPageState state) {
        userPresenter.switchToCheckOutfitView(state);
    }
}
