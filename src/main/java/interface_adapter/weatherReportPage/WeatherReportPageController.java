package interface_adapter.weatherReportPage;

import api.openWeatherApi.WeatherDataFetcher;
import api.geocodingapi.CoordinatesFetcher;
import use_case.currentWeather.CurrentWeatherInputBoundary;
import use_case.currentWeather.CurrentWeatherInputData;
import use_case.deleteFavouriteLocation.DeleteLocationInputBoundary;
import use_case.deleteFavouriteLocation.DeleteLocationInputData;

public class WeatherReportPageController {
    private WeatherReportPageViewModel weatherReportPageViewModel;
    private final CurrentWeatherInputBoundary currentWeatherInputBoundary;
    private final DeleteLocationInputBoundary deleteLocationInteractor;

    public WeatherReportPageController(
            WeatherReportPageViewModel viewModel,
            CurrentWeatherInputBoundary currentWeatherInputBoundary,
            DeleteLocationInputBoundary deleteLocationInteractor) {
        this.weatherReportPageViewModel = viewModel;
        this.currentWeatherInputBoundary = currentWeatherInputBoundary;
        this.deleteLocationInteractor = deleteLocationInteractor;
    }

    public void switchToLoggedInSearchView() {
        currentWeatherInputBoundary.switchToLoggedInSearchView();
    }

    public void switchToLoggedInHomePageView() {
        currentWeatherInputBoundary.switchToLoggedInHomePageView();
    }

    public void addToFavourites(WeatherReportPageState state) throws CoordinatesFetcher.CityNotFoundException, WeatherDataFetcher.CityNotFoundException {
        CurrentWeatherInputData inputData = new CurrentWeatherInputData(
                state.getCityName()
        );
        currentWeatherInputBoundary.addToFavourites(inputData);
    }

    public void removeFromFavourites() {
        String city = weatherReportPageViewModel.getState().getCityName();
        DeleteLocationInputData inputData = new DeleteLocationInputData(city);
        deleteLocationInteractor.execute(inputData);
    }

    public void resetPopUpMessage() {
        currentWeatherInputBoundary.resetPopUpMessage();
    }

    public void switchToCheckOutfitView(WeatherReportPageState state) {
        currentWeatherInputBoundary.switchToCheckOutfitView(state);
    }
}
