package use_case.rest;

import entity.WeatherCondition;

import java.time.LocalDate;

/**
 * The new day shown to the player after resting.
 */
public class RestOutputData {
    private final int dayNumber;
    private final LocalDate date;
    private final WeatherCondition condition;
    private final double highTemperature;
    private final double lowTemperature;
    private final int energy;
    private final int maxEnergy;
    private final boolean saved;

    public RestOutputData(int dayNumber, LocalDate date, WeatherCondition condition,
                          double highTemperature, double lowTemperature, int energy, int maxEnergy,
                          boolean saved) {
        this.dayNumber = dayNumber;
        this.date = date;
        this.condition = condition;
        this.highTemperature = highTemperature;
        this.lowTemperature = lowTemperature;
        this.energy = energy;
        this.maxEnergy = maxEnergy;
        this.saved = saved;
    }

    public int getDayNumber() {
        return dayNumber;
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

    public int getEnergy() {
        return energy;
    }

    public int getMaxEnergy() {
        return maxEnergy;
    }

    public boolean isSaved() {
        return saved;
    }
}
