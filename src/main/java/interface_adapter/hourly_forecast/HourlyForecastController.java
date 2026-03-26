package interface_adapter.hourly_forecast;

import use_case.hourly_forecast.HourlyForecastInputBoundary;

public class HourlyForecastController {
    private final HourlyForecastInputBoundary inputBoundary;

    public HourlyForecastController(HourlyForecastInputBoundary inputBoundary) {
        this.inputBoundary = inputBoundary;
    }

    public void SwitchToLoggedInHomePage() {
        inputBoundary.SwitchToLoggedInHomePage();
    }

    public void switchToWeatherReportView() {
        inputBoundary.switchToWeatherReportView();
    }
}
