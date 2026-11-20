package data_access;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

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
    private static final Duration TIMEOUT = Duration.ofSeconds(10);

    private final String token;
    private final int imageSize;
    private final HttpClient client = HttpClient.newBuilder().connectTimeout(TIMEOUT).build();

    public MapboxImageDataAccessObject(String token, int imageSize) {
        this.token = token;
        this.imageSize = imageSize;
    }

    @Override
    public byte[] getImage(City city, int zoomStep) throws DataAccessException {
        if (token == null || token.isBlank()) {
            throw new DataAccessException("MAPBOX_ACCESS_TOKEN is not set");
        }
        HttpRequest request = HttpRequest.newBuilder(URI.create(getImageURL(city, toMapboxZoom(zoomStep))))
                .timeout(TIMEOUT)
                .GET()
                .build();
        try {
            HttpResponse<byte[]> response = client.send(request, HttpResponse.BodyHandlers.ofByteArray());
            if (response.statusCode() == 401) {
                throw new DataAccessException("Mapbox rejected the access token");
            }
            if (response.statusCode() != 200) {
                throw new DataAccessException("Mapbox returned HTTP " + response.statusCode());
            }
            return response.body();
        } catch (IOException e) {
            throw new DataAccessException("Could not reach Mapbox", e);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new DataAccessException("Image request was interrupted", e);
        }
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
