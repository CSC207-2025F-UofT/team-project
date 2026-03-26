package interface_adapter.hourly_forecast;

import entity.hourly_forecast_report.HourlyForecastReport;

public class HourlyForecastState {
    private HourlyForecastReport report;
    private String cityName;

    public HourlyForecastReport getReport() {
        return report;
    }

    public void setReport(HourlyForecastReport report) {
        this.report = report;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }
}
