package use_case.currentWeather;

import entity.hourly_forecast_report.HourlyForecastReport;
import entity.weatherReport.WeatherReport;
import interface_adapter.weatherReportPage.WeatherReportPageState;

import java.util.List;

public interface CurrentWeatherOutputBoundary {
    void switchToLoggedInSearchView();

    void switchToLoggedInHomePageView();

    void switchToCheckOutfitView(WeatherReportPageState state);

    void addToFavouriteSuccess(CurrentWeatherOutputData outputData);

    void addToFavouriteFail(CurrentWeatherOutputData outputData);

    void resetPopUpMessage();

     void switchToFavouritesPageView(List<WeatherReport> weatherReports);

     void switchToHourlyForecast(HourlyForecastReport report, String cityName);
}
