package service;

import exception.WeatherDataLoadException;
import model.Location;
import model.WeatherData;

public class WeatherDataService {

    private final boolean simulateLoadFail;

    public WeatherDataService(boolean simulateLoadFail) {
        this.simulateLoadFail = simulateLoadFail;
    }

    public WeatherData loadWeatherData(Location location) throws WeatherDataLoadException {
        if (simulateLoadFail) {
            throw new WeatherDataLoadException("Network error.Failed to load weather data.");
        }
        // 这里模拟一下天气内容，真实系统应从接口获取
        String summary = "location：" + location.getName() + "，clouds 5.02°C，humidity 68%，feels like 1.2°C ";
        return new WeatherData(location, summary);
    }
}
