package sandbox;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Fetches 10 random dog images from The Dog API.
 * Docs: https://docs.thedogapi.com/reference/get_images_search
 */
public class ApiPlayground {

    // Replace with your real API key locally
    private static final String DOG_API_KEY = "REPLACE_WITH_YOUR_API_KEY";

    public static void main(String[] args) {
        try {
            HttpClient client = HttpClient.newHttpClient();

            // Request 10 images, medium size, JPG only
            String url = "https://api.thedogapi.com/v1/images/search"
                    + "?limit=10"
                    + "&size=med"
                    + "&mime_types=jpg";

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("Accept", "application/json")
                    .header("x-api-key", DOG_API_KEY)
                    .GET()
                    .build();

            HttpResponse<String> response =
                    client.send(request, HttpResponse.BodyHandlers.ofString());

            System.out.println("Status: " + response.statusCode());
            String json = response.body();
            System.out.println("Raw JSON:\n" + json + "\n");

            // Extract all "url":"..." entries from JSON
            Pattern p = Pattern.compile("\"url\":\"(.*?)\"");
            Matcher m = p.matcher(json);

            int count = 0;
            while (m.find()) {
                count++;
                System.out.println("Image " + count + ": " + m.group(1));
            }

            if (count == 0) {
                System.out.println("No image URLs found in response.");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
