package data_access;

import java.io.IOException;
import java.util.List;
import okhttp3.*;

public class DBDataAccessObject {
    private static final String BASE_URL = "https://app.ticketmaster.com/discovery/v2/events.json";
    private static final String API_KEY = System.getenv("DB_KEY");

    public void get() {
        // Getting music events from TicketMaster
        final OkHttpClient client = new OkHttpClient().newBuilder().build();
        HttpUrl.Builder urlBuilder = HttpUrl.parse(BASE_URL).newBuilder();
        urlBuilder.addQueryParameter("apikey", API_KEY);
        // Get only music events
        urlBuilder.addQueryParameter("classificationName", "music");
        final Request request = new Request.Builder().url(urlBuilder.build()).build();
        try {
            final Response response = client.newCall(request).execute();
            System.out.println(response.code());
            if (response.isSuccessful()) {
                System.out.println(response.body().string());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
