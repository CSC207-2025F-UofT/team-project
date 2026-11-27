package use_case.import_photos;

import entity.Location;

public interface GeocoderInterface {
    Location getLocation(String description);
}
