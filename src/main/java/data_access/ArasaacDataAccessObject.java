package data_access;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import entity.Pictogram;
import use_case.customize_board.PictogramSearchDataAccessInterface;
import use_case.customize_board.PictogramSearchException;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Retrieves pictograms from the ARASAAC API.
 */
public final class ArasaacDataAccessObject implements PictogramSearchDataAccessInterface {

    public static final String BASE_URL =
            "https://api.arasaac.org/api/pictograms/en/search/";
    public static final String IMAGE_URL_TEMPLATE =
            "https://static.arasaac.org/pictograms/%d/%d_500.png";

    private final HttpRequestExecutor requestExecutor;
    private final ObjectMapper objectMapper;

    public ArasaacDataAccessObject() {
        this(createHttpRequestExecutor(), new ObjectMapper());
    }

    ArasaacDataAccessObject(HttpRequestExecutor requestExecutor,
                            ObjectMapper objectMapper) {
        this.requestExecutor = Objects.requireNonNull(requestExecutor);
        this.objectMapper = Objects.requireNonNull(objectMapper);
    }

    @Override
    public List<Pictogram> searchPictograms(String keyword)
            throws PictogramSearchException {
        if (keyword == null || keyword.isBlank()) {
            throw new PictogramSearchException(
                    "A pictogram search keyword is required.", null);
        }

        String encodedKeyword = URLEncoder.encode(
                keyword.trim(), StandardCharsets.UTF_8);
        URI uri = URI.create(BASE_URL + encodedKeyword);
        HttpRequest request = HttpRequest.newBuilder(uri)
                .timeout(Duration.ofSeconds(10))
                .header("Accept", "application/json")
                .GET()
                .build();

        HttpResponse<String> response;
        try {
            response = requestExecutor.send(request);
        } catch (InterruptedException exception) {
            Thread.currentThread().interrupt();
            throw new PictogramSearchException(
                    "The ARASAAC request was interrupted.", exception);
        } catch (IOException | RuntimeException exception) {
            throw new PictogramSearchException(
                    "Could not reach the ARASAAC API.", exception);
        }

        if (response.statusCode() < 200 || response.statusCode() >= 300) {
            throw new PictogramSearchException(
                    "ARASAAC returned HTTP " + response.statusCode() + ".", null);
        }

        try {
            return parseSearchResults(response.body());
        } catch (IOException | RuntimeException exception) {
            throw new PictogramSearchException(
                    "Could not read the ARASAAC search response.", exception);
        }
    }

    List<Pictogram> parseSearchResults(String responseBody) throws IOException {
        JsonNode root = objectMapper.readTree(responseBody);
        if (!root.isArray()) {
            throw new IOException("ARASAAC response is not a JSON array");
        }

        List<Pictogram> pictograms = new ArrayList<>();
        for (JsonNode result : root) {
            JsonNode idNode = result.get("_id");
            if (idNode == null || !idNode.isIntegralNumber()) {
                continue;
            }

            String label = findLabel(result);
            if (label == null) {
                continue;
            }

            int id = idNode.asInt();
            String imageLocation = String.format(
                    IMAGE_URL_TEMPLATE, id, id);
            pictograms.add(new Pictogram(id, label, imageLocation));
        }
        return List.copyOf(pictograms);
    }

    private String findLabel(JsonNode result) {
        JsonNode keywords = result.get("keywords");
        if (keywords != null && keywords.isArray()) {
            for (JsonNode keyword : keywords) {
                JsonNode keywordNode = keyword.get("keyword");
                if (keywordNode != null && keywordNode.isTextual()
                        && !keywordNode.asText().isBlank()) {
                    return keywordNode.asText();
                }
            }
        }

        return null;
    }

    private static HttpRequestExecutor createHttpRequestExecutor() {
        HttpClient client = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(10))
                .followRedirects(HttpClient.Redirect.NORMAL)
                .build();
        return request -> client.send(
                request, HttpResponse.BodyHandlers.ofString());
    }

    @FunctionalInterface
    interface HttpRequestExecutor {
        HttpResponse<String> send(HttpRequest request)
                throws IOException, InterruptedException;
    }
}
