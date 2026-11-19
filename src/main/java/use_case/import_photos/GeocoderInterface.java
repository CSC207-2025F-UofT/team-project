package use_case;

import entity.Location;

public interface GeocoderInterface {
    Location getLocation(String description);
}
