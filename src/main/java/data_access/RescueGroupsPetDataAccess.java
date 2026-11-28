package data_access;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.List;

import javax.imageio.ImageIO;

import org.json.JSONException;
import org.json.JSONObject;

import entity.Pet;
import use_case.browse_pets.BrowsePetsDataAccessInterface;

/** Loads a read-only snapshot of adoptable cats near Toronto from RescueGroups. */
public final class RescueGroupsPetDataAccess implements BrowsePetsDataAccessInterface {
    private static final URI SEARCH = URI.create(
            "https://api.rescuegroups.org/v5/public/animals/search/available/cats/"
                    + "?include=orgs,pictures,breeds&sort=animals.distance&limit=50");
    private static final String MEDIA_TYPE = "application/vnd.api+json";
    private static final int TIMEOUT_MS = 5_000;
    private static final int MAX_RESPONSE_BYTES = 4_000_000;

    private final String apiKey;
    private volatile List<Pet> pets = List.of();

    /**
     * Creates the public listing source.
     * @param apiKey the RescueGroups public API key, or null to disable loading
     */
    public RescueGroupsPetDataAccess(String apiKey) {
        this.apiKey = apiKey == null ? "" : apiKey.strip();
    }

    /** @return whether a key is configured */
    public boolean isConfigured() {
        return !apiKey.isEmpty();
    }

    @Override
    public List<Pet> getAvailablePets() {
        return pets;
    }

    /**
     * Replaces the in-memory catalog from the Toronto radius search. Call on a worker thread.
     * @throws IOException if the request or response fails
     */
    public void refresh() throws IOException {
        if (isConfigured()) {
            try {
                final byte[] response = request(SEARCH, searchBody(), apiKey);
                pets = RescueGroupsPetMapper.read(new JSONObject(new String(response, StandardCharsets.UTF_8)));
            } catch (JSONException exception) {
                throw new IOException("RescueGroups returned an invalid listing response.", exception);
            }
        }
    }

    static JSONObject searchBody() {
        final JSONObject radius = new JSONObject()
                .put("coordinates", "43.6532,-79.3832")
                .put("kilometers", 50);
        return new JSONObject().put("data", new JSONObject().put("filterRadius", radius));
    }

    /**
     * Loads a photo hosted by RescueGroups without sending the API key.
     * @param source the photo URL from the listing
     * @return the decoded photo, or null when unavailable
     */
    public BufferedImage loadPhoto(String source) {
        try {
            final URI uri = URI.create(source);
            final String host = uri.getHost();
            final boolean rescueGroupsHost = host != null
                    && (host.equals("rescuegroups.org") || host.endsWith(".rescuegroups.org"));
            final boolean rescueGroupsBucket = "s3.amazonaws.com".equals(host)
                    && uri.getPath().startsWith("/filestore.rescuegroups.org/");
            if ("https".equalsIgnoreCase(uri.getScheme()) && uri.getUserInfo() == null
                    && (rescueGroupsHost || rescueGroupsBucket)) {
                return ImageIO.read(new ByteArrayInputStream(request(uri, null, "")));
            }
        } catch (IOException | IllegalArgumentException exception) {
            // A missing or invalid photo does not prevent browsing the listing.
        }
        return null;
    }

    private static byte[] request(URI uri, JSONObject body, String key) throws IOException {
        final HttpURLConnection connection = (HttpURLConnection) uri.toURL().openConnection();
        connection.setConnectTimeout(TIMEOUT_MS);
        connection.setReadTimeout(TIMEOUT_MS);
        connection.setInstanceFollowRedirects(false);
        try {
            if (body != null) {
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Authorization", key);
                connection.setRequestProperty("Content-Type", MEDIA_TYPE);
                connection.setRequestProperty("Accept", MEDIA_TYPE);
                connection.setDoOutput(true);
                try (OutputStream output = connection.getOutputStream()) {
                    output.write(body.toString().getBytes(StandardCharsets.UTF_8));
                }
            }
            final int status = connection.getResponseCode();
            if (status != HttpURLConnection.HTTP_OK) {
                throw new IOException("RescueGroups returned HTTP " + status + ".");
            }
            try (InputStream input = connection.getInputStream()) {
                final byte[] bytes = input.readNBytes(MAX_RESPONSE_BYTES + 1);
                if (bytes.length > MAX_RESPONSE_BYTES) {
                    throw new IOException("The RescueGroups response exceeded the size limit.");
                }
                return bytes;
            }
        } finally {
            connection.disconnect();
        }
    }
}
