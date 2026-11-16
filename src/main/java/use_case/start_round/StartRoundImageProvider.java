package use_case.start_round;

import entity.City;

public interface StartRoundImageProvider {
    String getImageURL(City city, int zoom);
}
