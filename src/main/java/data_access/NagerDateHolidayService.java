package data_access;

import use_case.monitoring.HolidayService;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class NagerDateHolidayService implements HolidayService {
    private static final String BASE_URL = "https://nagerholidays.com/api/v4/Holidays/";

    private final HttpClient client = HttpClient.newHttpClient();

    private final Map<String, Map<LocalDate, String>> cache = new HashMap<>();

    @Override
    public Optional<String> getHolidayName(LocalDate date, String countryCode) {
        String upperCaseCountry = countryCode.toUpperCase();
        String cacheCode = upperCaseCountry + "-" + date.getYear();
 9
        try {
            Map<LocalDate, String> holidays = cache.get(cacheCode);

            if (holidays == null) {
                holidays = loadHolidays(date.getYear(), upperCaseCountry);
                cache.put(cacheCode, holidays);
            }

            return Optional.ofNullable(holidays.get(date));

        } catch (HolidayApiException exception) {
            return Optional.empty();
        }
    }

    private Map<LocalDate, String> loadHolidays(int year, String countryCode) {
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(BASE_URL + countryCode + "/" + year)).GET().build();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() != 200) {
                throw new HolidayApiException("Holiday API returned HTTP " + response.statusCode());
            }

            JsonArray json = JsonParser.parseString(response.body()).getAsJsonArray();

            Map<LocalDate, String> holidays = new HashMap<>();

            for (JsonElement element : json) {
                LocalDate holidayDate = LocalDate.parse(element.getAsJsonObject().get("date").getAsString());

                String holidayName = element.getAsJsonObject().get("name").getAsString();

                holidays.put(holidayDate, holidayName);
            }

            return holidays;

        } catch (InterruptedException exception) {
            Thread.currentThread().interrupt();
            throw new HolidayApiException("Holiday API request was interrupted", exception);
        } catch (IOException exception) {
            throw new HolidayApiException("Could not load holiday data", exception);
        }
    }
}
