package data_access;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;

import org.json.JSONArray;
import org.json.JSONObject;
import entity.Location;
import use_case.search.SearchDataAccessInterface;

public class OSMDataAccessObject implements SearchDataAccessInterface {

    @Override
    public boolean existsByName(String locationName) throws Exception {
        String url = "https://nominatim.openstreetmap.org/search?q="
                + java.net.URLEncoder.encode(locationName, StandardCharsets.UTF_8)
                + "&format=json&limit=1";

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("User-Agent", "TripPlanner/1.0 (207 5-6)")
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        JSONArray array = new JSONArray(response.body());
        return !array.isEmpty();
    }

    @Override
    public void save(Location location) {
        // TODO document why this method is empty
    }

    @Override
    public Location get(String locationName) throws IOException, InterruptedException {
        String url = "https://nominatim.openstreetmap.org/search?q="
                + java.net.URLEncoder.encode(locationName, StandardCharsets.UTF_8)
                + "&format=json&limit=1";

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("User-Agent", "TripPlanner/1.0 (207 5-6)") // 必须加User-Agent
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        JSONArray array = new JSONArray(response.body());
        JSONObject obj = array.getJSONObject(0);
        String name = obj.getString("display_name");
        double lat = obj.getDouble("lat");
        double lon = obj.getDouble("lon");

        return new Location(name, lat, lon);
    }

    @Override
    public void setCurrentLocation(String locationName) {
        // TODO document why this method is empty
    }

    @Override
    public String getCurrentLocationName() {
        return "";
    }
}
