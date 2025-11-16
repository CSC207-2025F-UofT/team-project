package entity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.time.LocalDate;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.sql.Timestamp;
import java.util.ArrayList;

public class Event {
    final String id;
    final String name;
    final List<String> artists;
    final String venue;
    final String city;
    final String country;
    final LocalDate date;
    final int priceMin;
    final int priceMax;
    final String ticketUrl;
    final List<String> genres;

    public Event(String id, String name, List<String> artists, String venue, String city, String country, LocalDate date, int priceMin, int priceMax, String ticketUrl, List<String> genres) {
        this.id = id;
        this.name = name;
        this.artists = artists;
        this.venue = venue;
        this.city = city;
        this.country = country;
        this.date = date;
        this.priceMin = priceMin;
        this.priceMax = priceMax;
        this.ticketUrl = ticketUrl;
        this.genres = genres;
    }

    // Getters
    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<String> getArtists() {
        return artists;
    }

    public String getVenue() {
        return venue;
    }

    public String getCity() {
        return city;
    }

    public String getCountry() {
        return country;
    }
    public LocalDate getDate() {
        return date;
    }

    public int getPriceMin() {
        return priceMin;
    }

    public int getPriceMax() {
        return priceMax;
    }

    public String getTicketUrl() {
        return ticketUrl;
    }

    public List<String> getGenres() {
        return genres;
    }

    public static Event eventFromJson(JSONObject jsonEvent) {
        String id = jsonEvent.optString("id", "N/A");
        String name = jsonEvent.optString("name", "Unnamed Event");
        String ticketUrl = jsonEvent.optString("url", "#");

        LocalDate date = extractDateFromJson(jsonEvent);
        List<Integer> priceRange = extractPriceFromJson(jsonEvent);
        int priceMin = priceRange.get(0);
        int priceMax = priceRange.get(1);

        List<String> artists = extractArtists(jsonEvent);
        List<String> genres = extractGenres(jsonEvent);
        String venueName = extractVenueName(jsonEvent);
        String cityName = extractCity(jsonEvent);
        String countryName = extractCountry(jsonEvent);

        return new Event(id, name, artists, venueName, cityName, countryName, date, priceMin, priceMax, ticketUrl, genres);
    }

    private static JSONObject getEmbedded(JSONObject jsonEvent) {
        return jsonEvent.has("_embedded") ? jsonEvent.getJSONObject("_embedded") : null;
    }

    private static JSONObject getVenue(JSONObject embedded) {
        if (embedded != null && embedded.has("venues")) {
            // Safely return the first venue object
            JSONArray venues = embedded.getJSONArray("venues");
            if (!venues.isEmpty()) {
                return venues.getJSONObject(0);
            }
        }
        return null;
    }

    private static JSONArray getAttractions(JSONObject embedded) {
        if (embedded != null && embedded.has("attractions")) {
            return embedded.getJSONArray("attractions");
        }
        return null;
    }

    public static LocalDate extractDateFromJson(JSONObject jsonEvent) {
        LocalDate date = null;
        try {
            if (jsonEvent.has("dates")) {
                JSONObject start = jsonEvent.getJSONObject("dates").getJSONObject("start");
                String startDateTimeStr = start.optString("dateTime");
                if (!startDateTimeStr.isEmpty()) {
                    date = LocalDate.parse(startDateTimeStr.split("T")[0]);
                }
            }
        } catch (Exception e) {
            System.err.println("Error parsing date:" + e.getMessage());
        }
        return date;
    }


    public static List<Integer> extractPriceFromJson(JSONObject jsonEvent) {
        int priceMin = 0;
        int priceMax = 0;

        try {
            if (jsonEvent.has("priceRanges")) {
                JSONArray prices = jsonEvent.getJSONArray("priceRanges");
                if (!prices.isEmpty()) {
                    JSONObject priceRange = prices.getJSONObject(0);
                    priceMin = priceRange.optInt("min", 0);
                    priceMax = priceRange.optInt("max", 0);
                }
            }
        } catch (Exception e) {
            System.err.println("Error parsing price" + e.getMessage());
        }

        return List.of(priceMin, priceMax);

    }

    public static List<String> extractArtists(JSONObject jsonEvent) {
        List<String> artists = new ArrayList<>();
        JSONObject embedded = getEmbedded(jsonEvent);
        if (embedded != null && embedded.has("attractions")) {
            JSONArray artistArray = embedded.getJSONArray("attractions");
            for (int i = 0; i < artistArray.length(); i++) {
                JSONObject artist = artistArray.getJSONObject(i);
                artists.add(artist.getString("name"));
            }

        }
        return artists;
    }


    public static String extractVenueName(JSONObject jsonEvent) {
        JSONObject embedded = getEmbedded(jsonEvent);
        JSONObject venue = getVenue(embedded);

        return venue != null ? venue.optString("name", "unknown") : "N/A";
    }

    public static String extractCity(JSONObject jsonEvent) {
        JSONObject embedded = getEmbedded(jsonEvent);
        JSONObject venue = getVenue(embedded);

        if (venue != null && venue.has("city")) {
            return venue.getJSONObject("city").optString("name", "unknown city");
        }

        return "N/A";
    }

    public static String extractCountry(JSONObject jsonEvent) {
        JSONObject embedded = getEmbedded(jsonEvent);
        JSONObject venue = getVenue(embedded);

        if (venue != null && venue.has("country")) {
            return venue.getJSONObject("country").optString("name", "unknown country");
        }

        return "N/A";
    }

    public static List<String> extractGenres(JSONObject jsonEvent) {
        List<String> genres = new ArrayList<>();

        if (jsonEvent.has("classifications")) {
            JSONArray classifications = jsonEvent.getJSONArray("classifications");

            for (int i = 0; i < classifications.length(); i++) {
                JSONObject classification = classifications.getJSONObject(i);
                if (classification.has("genre")) {
                    genres.add(classification.getJSONObject("genre").optString("name"));
                }
                if (classification.has("subGenre")) {
                    genres.add(classification.getJSONObject("subGenre").optString("name"));
                }
            }
        }
        return genres;
    }

    @Override
    public String toString() {
        // Simple date formatter for human readability
        String formattedDate = date != null ? date.toString() : "TBD";
        String priceRange = (priceMin > 0 || priceMax > 0) ?
                String.format("Min: %d, Max: %d", priceMin, priceMax) : "N/A";

        return "--- EVENT DETAILS ---\n" +
                "Event Name: " + name + " (" + id + ")\n" +
                "Artist(s):  " + String.join(", ", artists) + "\n" +
                "Location:   " + city + ", " + country + "\n" +
                "Venue:      " + venue + "\n" +
                "Date/Time:  " + formattedDate + "\n" +
                "Genres:     " + String.join(", ", genres) + "\n" +
                "Price ($):  " + priceRange + "\n" +
                "Tickets:    " + ticketUrl + "\n" +
                "----------------------";
    }
}
