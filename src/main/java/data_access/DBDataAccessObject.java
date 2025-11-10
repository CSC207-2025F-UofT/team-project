package data_access;

import java.io.IOException;
import java.util.List;
import okhttp3.*;

public class DBDataAccessObject {
    private static final String BASE_URL = "https://app.ticketmaster.com/discovery/v2/events.json";
    private static final String API_KEY = System.getenv("DB_KEY");

    public String[] get() {
        final OkHttpClient client = new OkHttpClient().newBuilder().build();
        HttpUrl.Builder urlBuilder = HttpUrl.parse(BASE_URL).newBuilder();

        return new String[] {};
    }
}
