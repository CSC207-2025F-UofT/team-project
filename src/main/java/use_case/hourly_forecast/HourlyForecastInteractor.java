package use_case.hourly_forecast;

public class HourlyForecastInteractor implements HourlyForecastInputBoundary {
    private final HourlyForecastOutputBoundary presenter;

    public HourlyForecastInteractor(HourlyForecastOutputBoundary presenter) {
        this.presenter = presenter;
    }

    @Override
    public void SwitchToLoggedInHomePage() {
        presenter.SwitchToLoggedInHomePage();
    }

    @Override
    public void switchToWeatherReportView() {
        presenter.switchToWeatherReportView();
    }
}
