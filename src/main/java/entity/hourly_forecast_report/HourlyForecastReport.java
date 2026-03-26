package entity.hourly_forecast_report;

import java.util.List;

public class HourlyForecastReport {
    private final List<String> dateTimes;
    private final List<String> weather;
    private final List<String> temperature;
    private final List<String> percipitation;

    public HourlyForecastReport(List<String> dateTimes, List<String> weather,
                                List<String> temperature, List<String> percipitation) {
        this.dateTimes = dateTimes;
        this.weather = weather;
        this.temperature = temperature;
        this.percipitation = percipitation;
    }

    public List<String> getDateTimes() {
        return dateTimes;
    }

    public List<String> getWeather() {
        return weather;
    }

    public List<String> getTemperature() {
        return temperature;
    }

    public List<String> getPercipitation() {
        return percipitation;
    }
}
