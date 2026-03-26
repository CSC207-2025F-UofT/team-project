package interface_adapter.weatherReportPage;

import java.util.ArrayList;
import java.util.List;

public class WeatherReportPageState {
    private String cityName = "";
    private String weather = "";
    private String temperature = "";
    private String humidity = "";
    private String feelsLike = "";
    private String popUpMessage = "";
    private List<String> favouriteLocations = new ArrayList<>();


    public WeatherReportPageState(WeatherReportPageState copy) {
        cityName = copy.cityName;
        weather = copy.weather;
        temperature = copy.temperature;
        humidity = copy.humidity;
        feelsLike = copy.feelsLike;
    }
    public WeatherReportPageState() {}

    public String getCityName() { return cityName;}

    public String getWeather() { return weather;}

    public String getTemperature() { return temperature;}

    public String getHumidity() { return humidity;}

    public String getFeelsLike() { return feelsLike;}

    public List<String> getFavouriteLocations() {
        return favouriteLocations;
    }

    public String getPopUpMessage() {return popUpMessage;}

    public void setCityName(String cityName) { this.cityName = cityName;}

    public void setWeather(String weather) { this.weather = weather;}

    public void setTemperature(String temperature) { this.temperature = temperature;}

    public void setHumidity(String humidity) { this.humidity = humidity;}

    public void setFeelsLike(String feelsLike) {this.feelsLike = feelsLike;}

    public void setFavouriteLocations(List<String> favouriteLocations) {
        this.favouriteLocations = favouriteLocations;
    }

    public void setPopUpMessage(String popUpMessage) {this.popUpMessage = popUpMessage;}

    public void clearState(){
        cityName = "";
        weather = "";
        temperature = "";
        humidity = "";
        feelsLike = "";
        popUpMessage = "";
    }
}
