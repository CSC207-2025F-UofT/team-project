package interface_adapter.weatherReportPage;

import entity.weatherReport.WeatherReport;
import interface_adapter.ViewManagerModel;
import interface_adapter.loggedInHomePage.LoggedInHomePageViewModel;
import interface_adapter.loggedInSearchPage.LoggedInSearchPageViewModel;
import use_case.currentWeather.CurrentWeatherOutputBoundary;
import use_case.currentWeather.CurrentWeatherOutputData;

public class WeatherReportPagePresenter implements CurrentWeatherOutputBoundary {
    private final WeatherReportPageViewModel weatherReportPageViewModel;
    private final LoggedInHomePageViewModel loggedInHomePageViewModel;
    private final LoggedInSearchPageViewModel loggedInSearchPageViewModel;
    private final ViewManagerModel viewManagerModel;

    public WeatherReportPagePresenter(WeatherReportPageViewModel weatherReportPageViewModel,
                                      LoggedInSearchPageViewModel loggedInSearchPageViewModel,
                                      LoggedInHomePageViewModel loggedInHomePageViewModel,
                                      ViewManagerModel viewManagerModel) {
        this.weatherReportPageViewModel = weatherReportPageViewModel;
        this.loggedInSearchPageViewModel = loggedInSearchPageViewModel;
        this.loggedInHomePageViewModel = loggedInHomePageViewModel;
        this.viewManagerModel = viewManagerModel;
    }

    @Override
    public void switchToLoggedInSearchView() {
        viewManagerModel.setState(loggedInSearchPageViewModel.getViewName());
        viewManagerModel.firePropertyChanged();
    }

    @Override
    public void switchToLoggedInHomePageView() {
        viewManagerModel.setState(loggedInHomePageViewModel.getViewName());
        viewManagerModel.firePropertyChanged();
    }

    @Override
    public void addToFavouriteSuccess(CurrentWeatherOutputData outputData) {
        WeatherReportPageState state = weatherReportPageViewModel.getState();
        state.setPopUpMessage(outputData.getCityName() + " Successfully added to favourites.");
        weatherReportPageViewModel.firePropertyChanged();
    }

    @Override
    public void addToFavouriteFail(CurrentWeatherOutputData outputData) {
        WeatherReportPageState state = weatherReportPageViewModel.getState();
        state.setPopUpMessage(outputData.getCityName() + " is already saved to your favourites.");
        weatherReportPageViewModel.firePropertyChanged();
    }

    @Override
    public void resetPopUpMessage() {
        WeatherReportPageState state = weatherReportPageViewModel.getState();
        state.setPopUpMessage("");
    }
}