package data_access;

import entity.City;
import use_case.MapImageProvider;

public class MapboxImageProvider implements MapImageProvider {
    private final String token;
    private final int imageSize;

    public MapboxImageProvider(String token, int imageSize) {
        this.token = token;
        this.imageSize = imageSize;
    }

    @Override
    public String getImageURL(City city, int zoom) {
        return "https://api.mapbox.com/styles/v1/mapbox/satellite-v9/static/"
                + city.getLon() + "," + city.getLat() + "," + zoom + ",0,0"
                + "/" + imageSize + "x" + imageSize + "?access_token=" + token;
    }
}