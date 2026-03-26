package interface_adapter.weatherReportPage;

import entity.hourly_forecast_report.HourlyForecastReport;
import entity.weatherReport.WeatherReport;
import interface_adapter.ViewManagerModel;
import interface_adapter.checkOutfit.CheckOutfitState;
import interface_adapter.checkOutfit.CheckOutfitViewModel;
import interface_adapter.hourly_forecast.HourlyForecastState;
import interface_adapter.hourly_forecast.HourlyForecastViewModel;
import interface_adapter.loggedInFavouritesPage.LoggedInFavouritesPageState;
import interface_adapter.loggedInFavouritesPage.LoggedInFavouritesPageViewModel;
import interface_adapter.loggedInHomePage.LoggedInHomePageViewModel;
import interface_adapter.loggedInSearchPage.LoggedInSearchPageViewModel;
import use_case.currentWeather.CurrentWeatherOutputBoundary;
import use_case.currentWeather.CurrentWeatherOutputData;

import java.util.List;

public class WeatherReportPagePresenter implements CurrentWeatherOutputBoundary {
    private final WeatherReportPageViewModel weatherReportPageViewModel;
    private final LoggedInHomePageViewModel loggedInHomePageViewModel;
    private final LoggedInSearchPageViewModel loggedInSearchPageViewModel;
    private final CheckOutfitViewModel checkOutfitViewModel;
    private final LoggedInFavouritesPageViewModel loggedInFavouritesPageViewModel;
    private final HourlyForecastViewModel hourlyForecastViewModel;
    private final ViewManagerModel viewManagerModel;

    public WeatherReportPagePresenter(WeatherReportPageViewModel weatherReportPageViewModel,
                                      LoggedInSearchPageViewModel loggedInSearchPageViewModel,
                                      LoggedInHomePageViewModel loggedInHomePageViewModel,
                                      CheckOutfitViewModel checkOutfitViewModel,
                                      LoggedInFavouritesPageViewModel loggedInFavouritesPageViewModel, HourlyForecastViewModel hourlyForecastViewModel,
                                      ViewManagerModel viewManagerModel) {
        this.weatherReportPageViewModel = weatherReportPageViewModel;
        this.loggedInSearchPageViewModel = loggedInSearchPageViewModel;
        this.loggedInHomePageViewModel = loggedInHomePageViewModel;
        this.checkOutfitViewModel = checkOutfitViewModel;
        this.loggedInFavouritesPageViewModel = loggedInFavouritesPageViewModel;
        this.hourlyForecastViewModel = hourlyForecastViewModel;
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

    @Override
    public void switchToFavouritesPageView(List<WeatherReport> weatherReports) {
        LoggedInFavouritesPageState state = loggedInFavouritesPageViewModel.getState();
        state.setWeatherReports(weatherReports);
        loggedInFavouritesPageViewModel.setState(state);
        loggedInFavouritesPageViewModel.firePropertyChanged();
        viewManagerModel.setState(loggedInFavouritesPageViewModel.getViewName());
        viewManagerModel.firePropertyChanged();
    }

    @Override
    public void switchToHourlyForecast(HourlyForecastReport report, String cityName) {
        HourlyForecastState hourlyForecastState = hourlyForecastViewModel.getState();
        hourlyForecastState.setReport(report);
        hourlyForecastState.setCityName(cityName);
        hourlyForecastViewModel.setState(hourlyForecastState);
        hourlyForecastViewModel.firePropertyChanged();
        viewManagerModel.setState(hourlyForecastViewModel.getViewName());
        viewManagerModel.firePropertyChanged();
    }
}