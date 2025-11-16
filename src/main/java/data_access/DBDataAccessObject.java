package data_access;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.json.*;
import entity.Event;
import okhttp3.*;

import use_case.Create_event_list.EventDataAccessInterface;

/**
 * The DAO for fetching events from the TicketMaster API.
 */
public class DBDataAccessObject implements EventDataAccessInterface {
    private static final String BASE_URL = "https://app.ticketmaster.com/discovery/v2/events.json";
    private static final String API_KEY = System.getenv("DB_KEY");

    @Override
    public List<Event> getEvents(String keyword, String artist, String country, String city, String startDateTime, String endDateTime, String genre) {
        // Getting music events from TicketMaster
        final OkHttpClient client = new OkHttpClient().newBuilder().build();
        HttpUrl.Builder urlBuilder = HttpUrl.parse(BASE_URL).newBuilder();
        urlBuilder.addQueryParameter("apikey", API_KEY);
        // Get only music events
        urlBuilder.addQueryParameter("classificationName", "music");

        // Adding each query to url
        String keyw = "";
        boolean needsSpace = false;

        if (artist != null) {
            keyw = artist;
            needsSpace = true;
        }

        if (keyword != null) {
            if (needsSpace) {
                keyw = keyw + " "; // Add the space separator!
            }
            keyw = keyw + keyword;

            urlBuilder.addQueryParameter("keyword", keyw);
        } else {
            urlBuilder.addQueryParameter("keyword", keyw);
        }

        if (country != null) {
            urlBuilder.addQueryParameter("countryCode", country);
        }

        if (city != null) {
            urlBuilder.addQueryParameter("city", city);
        }

        if (startDateTime != null) {
            urlBuilder.addQueryParameter("startDateTime", startDateTime);
        }

        if (endDateTime != null) {
            urlBuilder.addQueryParameter("endDateTime", endDateTime);
        }

        if (genre != null) {
            urlBuilder.addQueryParameter("genreId", genre);
        }

        final Request request = new Request.Builder().url(urlBuilder.build()).build();
        try {
            final Response response = client.newCall(request).execute();
            System.out.println(response.code());
            if (response.isSuccessful()) {
                // Checking if body is not null
                assert response.body() != null;
                String dataJson = response.body().string();
                List<Event> events = createEventsFromJson(dataJson);
                return events;
            } else {
                throw new IOException("Something went wrong when fetching" + response);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;

    }

    public List<Event> createEventsFromJson(String dataJson) {
        List<Event> events = new ArrayList<>();

        if (dataJson == null || dataJson.isEmpty()) {
            return events;
        }

        try {
            // Root of json
            JSONObject base = new JSONObject(dataJson);

            if (base.has("_embedded") && base.getJSONObject("_embedded").has("events")) {
                JSONArray eventsArray = base.getJSONObject("_embedded").getJSONArray("events");
                // Each event in the events
                for (int i = 0; i < eventsArray.length(); i++) {
                    JSONObject jsonEvent = eventsArray.getJSONObject(i);

                    Event event = Event.eventFromJson(jsonEvent);
                    events.add(event);
                }
            }


        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return events;
    }
}
