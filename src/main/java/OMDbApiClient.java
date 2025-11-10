import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class OMDbApiClient {

    private static final String BASE_URL = "http://www.omdbapi.com/";
    private String apiKey;

    public OMDbApiClient(String key) {
        this.apiKey = key;
    }

    public String searchMovies(String name) throws IOException, InterruptedException {
        if (name==null || name.trim().equals("")) {
            throw new IllegalArgumentException("Empty search not allowed");
        }

        String url = BASE_URL + "?apikey=" + apiKey + "&s=" + name.replace(" ", "+");

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();

        HttpResponse<String> resp = client.send(req, HttpResponse.BodyHandlers.ofString());

        if(resp.statusCode() != 200) {
            throw new IOException("HTTP Error " + resp.statusCode());
        }

        return resp.body();
    }
}
