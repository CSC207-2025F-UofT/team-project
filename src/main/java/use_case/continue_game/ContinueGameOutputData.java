package use_case.continue_game;

import entity.WeatherCondition;

import java.time.LocalDate;

/**
 * The restored day shown at the residence. Weather fields are empty if the checkpoint has no weather yet.
 */
public class ContinueGameOutputData {
    private final int dayNumber;
    private final LocalDate date;
    private final WeatherCondition condition;
    private final double highTemperature;
    private final double lowTemperature;
    private final int energy;
    private final int maxEnergy;

    public ContinueGameOutputData(int dayNumber, LocalDate date, WeatherCondition condition,
                                  double highTemperature, double lowTemperature, int energy, int maxEnergy) {
        this.dayNumber = dayNumber;
        this.date = date;
        this.condition = condition;
        this.highTemperature = highTemperature;
        this.lowTemperature = lowTemperature;
        this.energy = energy;
        this.maxEnergy = maxEnergy;
    }

    public boolean hasWeather() {
        return condition != null;
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
}
