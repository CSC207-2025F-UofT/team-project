package use_case;

public class GetWeatherInputData {
    private final String name;
    private final String countryCode;
    private final double latitude;
    private final double longitude;

    public GetWeatherInputData(String name, String countryCode, double latitude, double longitude) {
        this.name = name;
        this.countryCode = countryCode;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getName() { return name; }
    public String getCountryCode() { return countryCode; }
    public double getLatitude() { return latitude; }
    public double getLongitude() { return longitude; }
}

