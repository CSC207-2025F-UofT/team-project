package use_case.import_photos;

import entity.LocationInfo;

public interface ReverseGeocoderInterface {
    LocationInfo getLocationInfo(double latitude, double longitude);
}
