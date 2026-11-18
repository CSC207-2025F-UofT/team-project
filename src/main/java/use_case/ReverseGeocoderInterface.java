package use_case;

import entity.LocationInfo;

public interface ReverseGeocoderInterface {
    LocationInfo getLocationInfo(double latitude, double longitude);
}
