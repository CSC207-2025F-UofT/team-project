package api.hourly_forecast_api;

import entity.hourly_forecast_report.HourlyForecastReport;

import java.util.HashMap;

public interface HourlyForecastFetcher {
    HourlyForecastReport getHourlyForecast(HashMap<String, Double> coordinates);

    class CityNotFoundException extends RuntimeException {
        public CityNotFoundException() {
            super("City not found: ");
        }
    }
}
