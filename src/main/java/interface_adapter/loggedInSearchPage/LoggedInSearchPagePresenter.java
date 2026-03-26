package interface_adapter.loggedInSearchPage;

import entity.placeSuggestions.PlaceSuggestion;
import entity.weatherReport.WeatherReport;
import interface_adapter.ViewManagerModel;
import interface_adapter.loggedInHomePage.LoggedInHomePageViewModel;
import interface_adapter.weatherReportPage.WeatherReportPageState;
import interface_adapter.weatherReportPage.WeatherReportPageViewModel;
import use_case.loggedInSearchPage.LoggedInSearchPageOutputBoundary;

import javax.swing.*;
import java.util.List;

public class LoggedInSearchPagePresenter implements LoggedInSearchPageOutputBoundary {
    private final LoggedInSearchPageViewModel loggedInSearchPageViewModel;
    private final LoggedInHomePageViewModel loggedInHomePageViewModel;
    private final WeatherReportPageViewModel weatherReportPageViewModel;
    private final ViewManagerModel viewManagerModel;

    public LoggedInSearchPagePresenter(LoggedInSearchPageViewModel loggedInSearchPageViewModel,
                                       LoggedInHomePageViewModel loggedInHomePageViewModel, WeatherReportPageViewModel weatherReportPageViewModel,
                                       ViewManagerModel viewManagerModel) {
        this.loggedInSearchPageViewModel = loggedInSearchPageViewModel;
        this.loggedInHomePageViewModel = loggedInHomePageViewModel;
        this.weatherReportPageViewModel = weatherReportPageViewModel;
        this.viewManagerModel = viewManagerModel;
    }

    @Override
    public void switchToLoggedInHomePageView() {
        viewManagerModel.setState(loggedInHomePageViewModel.getViewName());
        viewManagerModel.firePropertyChanged();
    }

    @Override
    public void showSuggestionsToUser(List<PlaceSuggestion> suggestions) {
        final LoggedInSearchPageState state = loggedInSearchPageViewModel.getState();
        state.setSuggestions(suggestions);
        loggedInSearchPageViewModel.setState(state);
        loggedInSearchPageViewModel.firePropertyChanged();
    }

    @Override
    public void switchToWeatherReportView(WeatherReport weatherReport) {
        // This function should be complete. The WeatherReportState will have all the information in weather report.
        final WeatherReportPageState state = weatherReportPageViewModel.getState();
        state.setCityName(weatherReport.getLocation());
        state.setWeather(weatherReport.getWeather());
        state.setTemperature(weatherReport.getTemperature());
        state.setHumidity(weatherReport.getHumidity());
        state.setFeelsLike(weatherReport.getFeelsLike());
        weatherReportPageViewModel.setState(state);
        weatherReportPageViewModel.firePropertyChanged();
        viewManagerModel.setState(weatherReportPageViewModel.getViewName());
        viewManagerModel.firePropertyChanged();
    }

    @Override
    public void prepareFailToExecute(String query) {
        LoggedInSearchPageState state = loggedInSearchPageViewModel.getState();
        state.setPopUpMessage(query + " City not found, please try a different city.");
        loggedInSearchPageViewModel.firePropertyChanged();
    }

    @Override
    public void resetPopUpMessage() {
        LoggedInSearchPageState state = loggedInSearchPageViewModel.getState();
        state.setPopUpMessage("");
    }
}