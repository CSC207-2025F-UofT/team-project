package app;

import location.LocationServiceInterface;
import location.LocationService;

public class Main {
    public static void main(String[] args) {
        LocationServiceInterface service = new LocationService();

        double lat = 43.706400;
        double lon = -79.398600;

        System.out.println(service.getLocationInfo(lat, lon));
    }
}
