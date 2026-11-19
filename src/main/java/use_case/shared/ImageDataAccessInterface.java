package use_case.shared;

import entity.City;

/**
 * Satellite images. Shared because Start Round and Submit Guess need exactly this one method.
 */
public interface ImageDataAccessInterface {
    /**
     * @param city     the city to show
     * @param zoomStep 1 (most zoomed in) to 5 (most zoomed out), see Round.getZoomStep()
     * @return the raw image bytes (e.g. PNG/JPEG)
     * @throws DataAccessException if the image can't be fetched
     */
    byte[] getImage(City city, int zoomStep) throws DataAccessException;
}
