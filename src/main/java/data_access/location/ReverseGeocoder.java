package data_access.location;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

import org.json.JSONException;
import org.json.JSONObject;

import entity.LocationInfo;
import exception.ReverseGeocodeException;
import use_case.ReverseGeocoderInterface;

public class ReverseGeocoder implements ReverseGeocoderInterface {
    private static final String NOMINATIM_BASE_URL = "https://nominatim.openstreetmap.org/reverse";

    private final HttpClient httpClient;

    public ReverseGeocoder() {
        httpClient = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(10))
            .build();
    }

    private static String getContinent(String countryCode, double latitude, double longitude) {
        if (countryCode == null || countryCode.isBlank()) {
            return null;
        }

        String code = countryCode.trim().toLowerCase();

        if (code.equals("ru")) return (longitude < 60.0) ? "Europe" : "Asia";
        if (code.equals("tr")) return (longitude < 29.05) ? "Europe" : "Asia";
        if (code.equals("kz")) return (longitude < 60.0) ? "Europe" : "Asia";
        if (code.equals("eg")) return (longitude < 32.3) ? "Africa" : "Asia";

        return switch (code) {
            case "us", "ca", "mx", "gl", "bz", "cr", "cu", "dm", "do", "gd", "gt", "ht", "hn",
                "jm", "ni", "pa", "kn", "lc", "vc", "tt", "bs", "bb", "ag" -> "North America";

            case "br", "ar", "co", "cl", "pe", "ve", "ec", "bo", "uy", "py", "gy", "sr" -> "South America";

            case "gb", "fr", "de", "it", "es", "ua", "pl", "ro", "nl", "be", "gr", "cz", "pt",
                "se", "hu", "at", "bg", "dk", "fi", "sk", "no", "ie", "hr", "ba", "md", "al",
                "lt", "si", "lv", "ee", "me", "lu", "mt", "is", "ad", "mc", "li", "sm", "va",
                "by", "rs", "cy" -> "Europe";

            case "cn", "in", "id", "pk", "bd", "jp", "ph", "vn", "ir", "th", "mm", "kr", "iq",
                "sa", "uz", "my", "np", "ye", "kp", "sy", "lk", "kh", "jo", "ae", "az", "tj",
                "il", "la", "kg", "tm", "ps", "lb", "om", "kw", "ge", "mn", "am", "qa", "bh",
                "bt", "mv", "bn", "sg" -> "Asia";

            case "ng", "et", "cd", "tz", "za", "ke", "ug", "dz", "sd", "ma", "ao", "mz",
                "gh", "mg", "cm", "ci", "ne", "bf", "ml", "mw", "zm", "sn", "so", "td",
                "gn", "rw", "bj", "bi", "tn", "ss", "tg", "sl", "ly", "mr", "er", "gm", "bw",
                "na", "ls", "gw", "mu", "sz", "dj", "km", "cv", "st", "sc" -> "Africa";

            case "au", "pg", "nz", "fj", "sb", "vu", "nc", "pf", "ws", "gu", "ki", "fm", "to",
                "mh", "pw", "tv", "nr" -> "Oceania";

            case "aq", "bv", "gs", "hm", "tf" -> "Antarctica";

            default -> null;
        };
    }

    public LocationInfo getLocationInfo(double latitude, double longitude) {
        String url = String.format("%s?format=json&lat=%f&lon=%f&zoom=10",
            NOMINATIM_BASE_URL, latitude, longitude);

        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(url))
            .header("User-Agent", "JourneyeApp/1.0 (Local Desktop App)")
            .header("Accept", "application/json")
            .GET()
            .build();

        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() != 200) {
                throw new ReverseGeocodeException("OSM Nominatim API failed [Status " + response.statusCode() + "]: " + response.body());
            }

            JSONObject body = new JSONObject(response.body());
            JSONObject address = body.getJSONObject("address");

            return new LocationInfo(
                address.optString("city"),
                address.optString("county"),
                address.optString("state"),
                address.optString("country"),
                getContinent(address.optString("country_code"), latitude, longitude)
            );

        } catch (IOException e) {
            throw new ReverseGeocodeException(e.getMessage(), e);

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();

            throw new ReverseGeocodeException(e.getMessage(), e);

        } catch (JSONException e) {
            throw new ReverseGeocodeException(e.getMessage(), e);
        }
    }
}
