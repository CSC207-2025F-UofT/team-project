package interface_adapter.rest;

import entity.WeatherCondition;

import java.time.LocalDate;

/**
 * Shared text formatting for the residence screen.
 */
public final class ResidenceFormat {
    private ResidenceFormat() {
    }

    public static String day(int dayNumber, LocalDate date) {
        return String.format("Day %d (%s)", dayNumber, date);
    }

    public static String weather(WeatherCondition condition, double high, double low) {
        return String.format("Weather: %s, %.1f°C / %.1f°C", condition, high, low);
    }

    public static String noWeather() {
        return "Weather: -";
    }

    public static String energy(int energy, int maxEnergy) {
        return String.format("Energy: %d / %d", energy, maxEnergy);
    }
}
