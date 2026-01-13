package interface_adapter.weatherReportPage;

import interface_adapter.ViewManagerModel;
import interface_adapter.checkOutfit.CheckOutfitState;
import interface_adapter.checkOutfit.CheckOutfitViewModel;
import interface_adapter.loggedInHomePage.LoggedInHomePageViewModel;
import interface_adapter.loggedInSearchPage.LoggedInSearchPageViewModel;
import use_case.currentWeather.CurrentWeatherOutputBoundary;
import use_case.currentWeather.CurrentWeatherOutputData;

public class WeatherReportPagePresenter implements CurrentWeatherOutputBoundary {
    private final WeatherReportPageViewModel weatherReportPageViewModel;
    private final LoggedInHomePageViewModel loggedInHomePageViewModel;
    private final LoggedInSearchPageViewModel loggedInSearchPageViewModel;
    private final ViewManagerModel viewManagerModel;
    private final CheckOutfitViewModel checkOutfitViewModel;

    public WeatherReportPagePresenter(WeatherReportPageViewModel weatherReportPageViewModel,
                                      LoggedInSearchPageViewModel loggedInSearchPageViewModel,
                                      LoggedInHomePageViewModel loggedInHomePageViewModel,
                                      ViewManagerModel viewManagerModel,
                                      CheckOutfitViewModel checkOutfitViewModel) {
        this.weatherReportPageViewModel = weatherReportPageViewModel;
        this.loggedInSearchPageViewModel = loggedInSearchPageViewModel;
        this.loggedInHomePageViewModel = loggedInHomePageViewModel;
        this.viewManagerModel = viewManagerModel;
        this.checkOutfitViewModel = checkOutfitViewModel;
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
    public void switchToCheckOutfitView(WeatherReportPageState state) {
        CheckOutfitState state1 = checkOutfitViewModel.getState();
        state1.setCityName(state.getCityName());
        state1.setHumidity(state.getHumidity());
        state1.setTemperature(state.getTemperature());
        state1.setFeelsLike(state.getFeelsLike());
        state1.setWeather(state.getWeather());
        checkOutfitViewModel.firePropertyChanged();
        viewManagerModel.setState(checkOutfitViewModel.getViewName());
        viewManagerModel.firePropertyChanged();
    }

    @Override
    public void addToFavouriteSuccess(CurrentWeatherOutputData outputData) {
        WeatherReportPageState state = weatherReportPageViewModel.getState();
        state.setPopUpMessage(outputData.getCityName() + " successfully added to favourites.");
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