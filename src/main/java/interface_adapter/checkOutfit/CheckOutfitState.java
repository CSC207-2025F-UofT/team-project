package interface_adapter.checkOutfit;

public class CheckOutfitState {

    private String cityName = "";
    private String weather = "";
    private String temperature = "";
    private String humidity = "";
    private String feelsLike = "";
    private String popUpMessage = "";


    // getters
    public String getCityName() { return cityName; }
    public String getWeather() { return weather; }
    public String getTemperature() { return temperature; }
    public String getHumidity() { return humidity; }
    public String getFeelsLike() { return feelsLike; }
    public String getPopUpMessage() { return popUpMessage; }

    // setters
    public void setCityName(String cityName) { this.cityName = cityName; }
    public void setWeather(String weather) { this.weather = weather; }
    public void setTemperature(String temperature) { this.temperature = temperature; }
    public void setHumidity(String humidity) { this.humidity = humidity; }
    public void setFeelsLike(String feelsLike) { this.feelsLike = feelsLike; }
    public void setPopUpMessage(String popUpMessage) { this.popUpMessage = popUpMessage; }

}
