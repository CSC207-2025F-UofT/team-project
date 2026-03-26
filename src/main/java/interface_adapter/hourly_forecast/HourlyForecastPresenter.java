package interface_adapter.hourly_forecast;

import interface_adapter.ViewManagerModel;
import interface_adapter.loggedInHomePage.LoggedInHomePageViewModel;
import interface_adapter.weatherReportPage.WeatherReportPageViewModel;
import use_case.hourly_forecast.HourlyForecastOutputBoundary;

public class HourlyForecastPresenter implements HourlyForecastOutputBoundary {
    private final HourlyForecastViewModel hourlyForecastViewModel;
    private final LoggedInHomePageViewModel loggedInHomePageViewModel;
    private final WeatherReportPageViewModel weatherReportPageViewModel;
    private final ViewManagerModel viewManagerModel;

    public HourlyForecastPresenter(HourlyForecastViewModel hourlyForecastViewModel, LoggedInHomePageViewModel loggedInHomePageViewModel, WeatherReportPageViewModel weatherReportPageViewModel,
                                   ViewManagerModel viewManagerModel) {
        this.hourlyForecastViewModel = hourlyForecastViewModel;
        this.loggedInHomePageViewModel = loggedInHomePageViewModel;
        this.weatherReportPageViewModel = weatherReportPageViewModel;
        this.viewManagerModel = viewManagerModel;
    }

    @Override
    public void SwitchToLoggedInHomePage() {
        viewManagerModel.setState(loggedInHomePageViewModel.getViewName());
        viewManagerModel.firePropertyChanged();
    }

    @Override
    public void switchToWeatherReportView() {
        viewManagerModel.setState(weatherReportPageViewModel.getViewName());
        viewManagerModel.firePropertyChanged();
    }
}
