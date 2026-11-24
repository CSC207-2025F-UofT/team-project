package data_access;

import java.io.IOException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import entities.RegularUser;
import entities.Researcher;
import entities.User;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import use_case.login.LoginUserDataAccessInterface;
import use_case.signup.SignupUserDataAccessInterface;

public class SupabaseUserDataAccessObject
        implements SignupUserDataAccessInterface, LoginUserDataAccessInterface {
    private static final String BASE_URL =
            "https://lycetkmgbgkdgcbxhezw.supabase.co/rest/v1/Users";
    private static final String API_KEY = "sb_publishable_0dPA21pu1aHGE195Aw8YVw_lm45wA_b";
    private static final MediaType JSON = MediaType.get("application/json");

    private final OkHttpClient client = new OkHttpClient();

    @Override
    public boolean existsByName(String username) {
        final HttpUrl url = HttpUrl.parse(BASE_URL).newBuilder()
                .addQueryParameter("select", "username")
                .addQueryParameter("username", "eq." + username)
                .build();
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
            return new JSONArray(body).length() > 0;
        }
        catch (IOException | JSONException ex) {
            throw new RuntimeException("Failed to check username", ex);
        }
    }

    @Override
    public void save(User user) {
        final JSONObject json = new JSONObject();
        json.put("username", user.getUsername());
        json.put("role", user.getRole());

        final Request request = new Request.Builder()
                .url(BASE_URL)
                .addHeader("apikey", API_KEY)
                .addHeader("Prefer", "return=minimal")
                .post(RequestBody.create(json.toString(), JSON))
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                final String body = response.body() != null ? response.body().string() : "";
                throw new RuntimeException("HTTP " + response.code() + ": " + body);
            }
        }
        catch (IOException ex) {
            throw new RuntimeException("Failed to save user", ex);
        }
    }

    @Override
    public User get(String username) {
        final HttpUrl url = HttpUrl.parse(BASE_URL).newBuilder()
                .addQueryParameter("select", "username,role")
                .addQueryParameter("username", "eq." + username)
                .build();
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
            if (rows.length() == 0) {
                return null;
            }
            final JSONObject row = rows.getJSONObject(0);
            final String name = row.getString("username");
            return "Researcher".equals(row.optString("role")) ? new Researcher(name) : new RegularUser(name);
        }
        catch (IOException | JSONException ex) {
            throw new RuntimeException("Failed to load user", ex);
        }
    }
}
