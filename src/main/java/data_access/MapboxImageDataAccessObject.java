package data_access;

import entity.City;
import use_case.shared.DataAccessException;
import use_case.shared.ImageDataAccessInterface;

/**
 * Satellite images from the Mapbox Static Images API.
 * The token comes from the MAPBOX_ACCESS_TOKEN environment variable (read in AppBuilder) and is never committed.
 */
public class MapboxImageDataAccessObject implements ImageDataAccessInterface {
    /** Mapbox zoom level for zoom step 1 (most zoomed in). */
    private static final int START_ZOOM = 17;
    /** How far each zoom step zooms out. */
    private static final int ZOOM_OUT_PER_STEP = 1;

    private final String token;
    private final int imageSize;

    public MapboxImageDataAccessObject(String token, int imageSize) {
        this.token = token;
        this.imageSize = imageSize;
    }

    @Override
    public byte[] getImage(City city, int zoomStep) throws DataAccessException {
        throw new DataAccessException("Not implemented");
    }

    /** Zoom step 1..5 -> Mapbox zoom 17..13. */
    static int toMapboxZoom(int zoomStep) {
        return START_ZOOM - (zoomStep - 1) * ZOOM_OUT_PER_STEP;
    }

    public String getImageURL(City city, int zoom) {
        return "https://api.mapbox.com/styles/v1/mapbox/satellite-v9/static/"
            + city.getLon() + "," + city.getLat() + "," + zoom + ",0,0"
            + "/" + imageSize + "x" + imageSize + "?access_token=" + token;
    }
}
