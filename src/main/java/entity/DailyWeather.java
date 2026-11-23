package entity;

import java.time.LocalDate;

/**
 * One game day's weather. Immutable.
 */
public class DailyWeather {
    private final LocalDate date;
    private final WeatherCondition condition;
    private final double highTemperature;
    private final double lowTemperature;

    public DailyWeather(LocalDate date, WeatherCondition condition, double highTemperature, double lowTemperature) {
        this.date = date;
        this.condition = condition;
        this.highTemperature = highTemperature;
        this.lowTemperature = lowTemperature;
    }

    public int getEnergyRestorePercent() {
        return condition.getEnergyRestorePercent();
    }

    public LocalDate getDate() {
        return date;
    }

    public WeatherCondition getCondition() {
        return condition;
    }

    public double getHighTemperature() {
        return highTemperature;
    }

    public double getLowTemperature() {
        return lowTemperature;
    }
}
