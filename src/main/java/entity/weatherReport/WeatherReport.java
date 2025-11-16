package entity.weatherReport;

public class WeatherReport {
    //String latitude;
    //String longitude;
    private String location;
    // cloudy, rainy, sunny...
    private String weather; // (main in api)
    private String temperature;
    private String feelsLike;
    // String temp_min;
    // String temp_max; // String pressure;
    private String humidity;
    // String sea_level;
    // String ground_level;
    //String wind_speed;
    //String wind_deg;
    //String wind_gust;
    //String country;
    //String timezone;

    public WeatherReport(String location, String weather, String temperature,
                         String humidity, String feelsLike) {
        this.location = location;
        this.weather = weather;
        this.temperature = temperature;
        this.feelsLike = feelsLike;
        this.humidity = humidity;
    }

    @Override
    public String toString() {
        return "WeatherReport{" + "location:'" + location + '\''
                + ", weather:'" + weather + '\''
                + ", temperature:'" + temperature + '\''
                + ", Feels like:'" + feelsLike + '\''
                + ", humidity:" + humidity + '}';
    }

    public String getLocation() {
        return this.location;
    }

    public String getWeather() {
        return this.weather;
    }

    public String getTemperature() {
        return this.temperature;
    }

    public String getHumidity() {
        return this.humidity;
    }

    public String getFeelsLike() {
        return this.feelsLike;
    }

    public static class WeatherReportBuilder {
        private String location;
        private String weather; // (main in api)
        private String temperature;
        private String feelsLike;
        private String humidity;

        WeatherReportBuilder() {
        }

        public WeatherReportBuilder location(String location) {
            this.location = location;
            return this;
        }

        public WeatherReportBuilder weather(String weather) {
            this.location = weather;
            return this;
        }

        public WeatherReportBuilder temperature(String temperature) {
            this.temperature = temperature;
            return this;
        }

        public WeatherReportBuilder feelsLike(String feelsLike) {
            this.feelsLike = feelsLike;
            return this;
        }

        public WeatherReportBuilder humidity(String humidity) {
            this.humidity = humidity;
            return this;
        }

        public WeatherReport build() {
            return new WeatherReport(location, weather, temperature, feelsLike,
                    humidity);
        }

    }

    public static WeatherReportBuilder builder() {
        return new WeatherReportBuilder();
    }
}