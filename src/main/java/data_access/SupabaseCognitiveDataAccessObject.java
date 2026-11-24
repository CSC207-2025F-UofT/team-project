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
    /** The two cognitive test types a row can hold. */
    public static final String ATTENTION = "Attention";
    public static final String MEMORY = "Memory";
    /** Name of the CogSci column that says which test the score belongs to. */
    private static final String TEST_TYPE_COLUMN = "test_type";
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
            throw new RuntimeException("Failed to fetch cognitive data: " + ex.getMessage(), ex);
        }
    }

    private CognitiveData toEntity(JSONObject row) throws JSONException {
        // Tolerate both "2026-11-24" and "2026-11-24T00:00:00" style values.
        final String rawDate = row.getString("date");
        final Date date = Date.from(LocalDate.parse(rawDate.substring(0, 10))
                .atStartOfDay(ZoneId.systemDefault()).toInstant());

        return new CognitiveData.CogBuilder(date)
                .mood(row.optString("mood", "none"))
                .productivityRating(row.optDouble("productivity", 0.0))
                .cogTest(toTestType(row.optString(TEST_TYPE_COLUMN, "")), row.optDouble("score", 0.0))
                .build();
    }

    /**
     * Converts the stored test type to "Attention" or "Memory" (case-insensitive).
     * @param raw the value stored in the table
     * @return {@link #ATTENTION} or {@link #MEMORY}
     * @throws JSONException if the value is neither
     */
    static String toTestType(String raw) throws JSONException {
        final String cleaned = raw.trim();
        if (cleaned.equalsIgnoreCase(ATTENTION)) {
            return ATTENTION;
        }
        if (cleaned.equalsIgnoreCase(MEMORY)) {
            return MEMORY;
        }
        throw new JSONException("Unknown test type '" + raw + "' in column '" + TEST_TYPE_COLUMN
                + "' (expected " + ATTENTION + " or " + MEMORY + ")");
    }
}
