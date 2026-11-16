package app;

import data_access.GameDataAccessObject;
import data_access.JSONCityDataAccessObject;
import data_access.MapboxImageProvider;

public class Main {
    public static void main(String[] args) {
        GameDataAccessObject dataAccessObject = new GameDataAccessObject(
                new JSONCityDataAccessObject("data/answer-cities.json", "data/guessable-cities.json")
        );

        String token = System.getenv("MAPBOX_ACCESS_TOKEN");
        final int IMAGE_SIZE = 640;

        if (token == null || token.isBlank()) {
            throw new IllegalStateException("MAPBOX_ACCESS_TOKEN is not set");
        }

        MapboxImageProvider imageProvider = new MapboxImageProvider(
                token,
                IMAGE_SIZE
        );
    }
}
