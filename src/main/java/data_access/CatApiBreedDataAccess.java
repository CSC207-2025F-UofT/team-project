package data_access;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import use_case.manage_pet.BreedReference;
import use_case.manage_pet.BreedReferenceDataAccessInterface;

/** Reads breed suggestions and optional reference photos from TheCatAPI. */
public final class CatApiBreedDataAccess implements BreedReferenceDataAccessInterface {
    private static final URI API_BASE = URI.create("https://api.thecatapi.com/v1/");
    private static final int TIMEOUT_MS = 4_000;
    private static final int MAX_JSON_BYTES = 512_000;
    private static final int MAX_PHOTO_BYTES = 4_000_000;

    private final String apiKey;
    private final ConnectionFactory connections;

    /**
     * Creates the adapter for TheCatAPI.
     * @param apiKey an optional key for TheCatAPI
     */
    public CatApiBreedDataAccess(String apiKey) {
        this(apiKey, uri -> (HttpURLConnection) uri.toURL().openConnection());
    }

    CatApiBreedDataAccess(String apiKey, ConnectionFactory connections) {
        this.apiKey = apiKey == null ? "" : apiKey.strip();
        this.connections = connections;
    }

    @Override
    public Optional<BreedReference> findByName(String name) {
        try {
            final String query = URLEncoder.encode(name, StandardCharsets.UTF_8);
            final JSONArray breeds = new JSONArray(getJson("breeds/search?q=" + query));
            for (int index = 0; index < breeds.length(); index++) {
                final JSONObject breed = breeds.getJSONObject(index);
                final String breedName = breed.getString("name");
                if (breedName.equalsIgnoreCase(name)) {
                    return Optional.of(new BreedReference(breedName, breed.optString("temperament"),
                            loadPhoto(breed)));
                }
            }
            return Optional.empty();
        } catch (IOException | JSONException exception) {
            throw new IllegalStateException("Breed lookup is unavailable. You can still fill in the form.", exception);
        }
    }

    private byte[] loadPhoto(JSONObject breed) {
        try {
            final JSONObject image = breed.optJSONObject("image");
            String photoUrl = image == null ? "" : image.optString("url");
            final String imageId = breed.optString("reference_image_id");
            if (photoUrl.isBlank() && imageId.matches("[A-Za-z0-9_-]+")) {
                photoUrl = new JSONObject(getJson("images/" + imageId)).optString("url");
            }
            final URI photo = URI.create(photoUrl);
            if ("https".equals(photo.getScheme()) && ("cdn2.thecatapi.com".equalsIgnoreCase(photo.getHost())
                    || "cdn.thecatapi.com".equalsIgnoreCase(photo.getHost()))) {
                return download(photo, "", MAX_PHOTO_BYTES);
            }
        } catch (IOException | JSONException | IllegalArgumentException | IllegalStateException exception) {
            // Photo availability should not prevent showing the breed's temperament.
        }
        return new byte[0];
    }

    private String getJson(String endpoint) throws IOException {
        return new String(download(API_BASE.resolve(endpoint), apiKey, MAX_JSON_BYTES), StandardCharsets.UTF_8);
    }

    private byte[] download(URI uri, String requestKey, int maxBytes) throws IOException {
        final HttpURLConnection connection = connections.open(uri);
        connection.setConnectTimeout(TIMEOUT_MS);
        connection.setReadTimeout(TIMEOUT_MS);
        connection.setInstanceFollowRedirects(false);
        if (!requestKey.isEmpty()) {
            connection.setRequestProperty("x-api-key", requestKey);
        }
        try {
            final int status = connection.getResponseCode();
            if (status == HttpURLConnection.HTTP_UNAUTHORIZED || status == HttpURLConnection.HTTP_FORBIDDEN) {
                throw new IllegalStateException("TheCatAPI denied the request. Check THE_CAT_API_KEY and try again.");
            }
            if (status != HttpURLConnection.HTTP_OK) {
                throw new IOException("Unexpected HTTP status: " + status);
            }
            try (InputStream body = connection.getInputStream()) {
                final byte[] bytes = body.readNBytes(maxBytes + 1);
                if (bytes.length > maxBytes) {
                    throw new IOException("TheCatAPI response exceeded the size limit.");
                }
                return bytes;
            }
        } finally {
            connection.disconnect();
        }
    }

    @FunctionalInterface
    interface ConnectionFactory {
        HttpURLConnection open(URI uri) throws IOException;
    }
}
