package entity.hourly_forecast_report;

import java.util.List;

public class HourlyForecastFactory {

    public HourlyForecastReport create(List<String> dateTimes, List<String> weather,
                                       List<String> temperature, List<String> percipitation) {
        return new HourlyForecastReport(dateTimes, weather, temperature, percipitation);
    }

}
