package use_case;

import entity.City;

public interface MapImageProvider {
    String getImageURL(City city, int zoom);
}
