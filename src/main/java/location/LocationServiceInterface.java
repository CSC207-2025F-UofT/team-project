package location;

import entity.LocationInfo;

public interface LocationServiceInterface {
    LocationInfo getLocationInfo(double latitude, double longitude) throws LocationServiceException;
}
