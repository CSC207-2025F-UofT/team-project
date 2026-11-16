package entity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class WeatherData {
    private final ArrayList<String> conditions = new ArrayList<>(Arrays.asList("Thunderstorm", "Drizzle",
            "Rain", "Snow", "Mist", "Smoke", "Haze", "Dust", "Fog", "Sand", "Ash", "Squall",
            "Tornado", "Clear", "Clouds"));

    private final double temperature;
    private final String condition;
    private final List<Integer> warmthLevels;

    public WeatherData(double temperature, String condition) {
        this.temperature = temperature;
        this.condition = condition;
        int roundedTemp = Math.toIntExact(Math.round(temperature));
        if (roundedTemp > 30) {
            this.warmthLevels = List.of(10);
        } else if (roundedTemp < -10) {
            this.warmthLevels = List.of(-10);
        } else {
            if (roundedTemp % 2 != 0) {
                roundedTemp += 1;
            }
            int median = roundedTemp/2 - 5;
            this.warmthLevels = List.of(median - 1, median, median + 1);
        }
    }

    public double getTemperature() {
        return this.temperature;
    }
    public String getCondition() {
        return this.condition;
    }
    public List<Integer> getWarmthLevels() {
        return this.warmthLevels;
    }
    public ArrayList<String> getConditions() {
        return this.conditions;
    }

}
