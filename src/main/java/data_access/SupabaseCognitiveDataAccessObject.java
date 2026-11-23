package data_access;

import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import entities.CognitiveData;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import use_case.get_data.get_dataDataAccessInterface;

public class SupabaseCognitiveDataAccessObject implements get_dataDataAccessInterface {
    private static final String BASE_URL =
            "https://lycetkmgbgkdgcbxhezw.supabase.co/rest/v1/CogSci";
    // TODO change this for 2 tests idk how to do this
    private static final String TEST_TYPE = "";
    private final OkHttpClient client = new OkHttpClient();
    private static final String API_KEY = "sb_publishable_0dPA21pu1aHGE195Aw8YVw_lm45wA_b";

    @Override
    public List<CognitiveData> fetchAll() {
        final HttpUrl url = HttpUrl.parse(BASE_URL).newBuilder()
                .addQueryParameter("select", "*")
                .addQueryParameter("order", "date.asc")
                .build();
        return fetch(url);
    }

    @Override
    public List<CognitiveData> fetchByUsername(String username) {
        final HttpUrl url = HttpUrl.parse(BASE_URL).newBuilder()
                .addQueryParameter("select", "*")
                .addQueryParameter("order", "date.asc")
                .addQueryParameter("username", "eq." + username)
                .build();
        return fetch(url);
    }

    private List<CognitiveData> fetch(HttpUrl url) {
        final Request request = new Request.Builder()
                .url(url)
                .addHeader("apikey", API_KEY)
                .addHeader("Accept", "application/json")
                .build();

        try (Response response = client.newCall(request).execute()) {
            final String body = response.body() != null ? response.body().string() : "[]";

            if (!response.isSuccessful()) {
                throw new RuntimeException("HTTP " + response.code() + ": " + body);
            }

            final JSONArray rows = new JSONArray(body);
            final List<CognitiveData> result = new ArrayList<>();

            for (int i = 0; i < rows.length(); i++) {
                result.add(toEntity(rows.getJSONObject(i)));
            }
            return result;
        }
        catch (IOException | JSONException ex) {
            throw new RuntimeException("Failed to fetch cognitive data", ex);
        }
    }

    private CognitiveData toEntity(JSONObject row) throws JSONException {
        final Date date = Date.from(LocalDate.parse(row.getString("date"))
                .atStartOfDay(ZoneId.systemDefault()).toInstant());

        return new CognitiveData.CogBuilder(date)
                .mood(row.getString("mood"))
                .productivityRating(row.getDouble("productivity"))
                .cogTest(TEST_TYPE, row.getDouble("score"))
                .build();
    }
}