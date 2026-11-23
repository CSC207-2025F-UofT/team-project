package data_access;

import org.json.JSONException;
import org.json.JSONObject;
import use_case.get_pet_picture.PetImageDataAccessInterface;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

public class ApiPetImageDataAccessObject
        implements PetImageDataAccessInterface {

    private final HttpClient client = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(10))
            .build();

    @Override
    public String fetchImageUrl(String species) throws IOException {
        final String endpoint;
        final String imageKey;

        if ("DOG".equals(species)) {
            endpoint = "https://dog.ceo/api/breeds/image/random";
            imageKey = "message";
        } else if ("CAT".equals(species)) {
            endpoint = "https://cataas.com/cat?json=true";
            imageKey = "url";
        } else {
            throw new IOException("Unsupported species: " + species);
        }

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(endpoint))
                .timeout(Duration.ofSeconds(15))
                .header("Accept", "application/json")
                .GET()
                .build();

        final HttpResponse<String> response;

        try {
            response = client.send(
                    request,
                    HttpResponse.BodyHandlers.ofString()
            );
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IOException("Picture request interrupted.", e);
        }

        if (response.statusCode() != 200) {
            throw new IOException(
                    "Picture API returned HTTP " + response.statusCode()
            );
        }

        try {
            JSONObject json = new JSONObject(response.body());

            if ("DOG".equals(species)
                    && !"success".equals(json.optString("status"))) {
                throw new IOException("Dog API reported a failure.");
            }

            String imageUrl = json.getString(imageKey);

            if (imageUrl.trim().isEmpty()) {
                throw new IOException("Picture URL was empty.");
            }

            return imageUrl;
        } catch (JSONException e) {
            throw new IOException("Invalid picture API response.", e);
        }
    }
}