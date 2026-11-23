
package data_access;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.RequestBody;
import okhttp3.MediaType;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;

import org.json.JSONArray;
import org.json.JSONObject;

public class GetMovieData {

    private static final String BASE_URL =
            "https://api.themoviedb.org/3/";

    private final OkHttpClient client;
    private final String token;

    // Constructor
    public GetMovieData(String token) {
        if (token == null || token.isBlank()) {
            throw new IllegalArgumentException(
                    "TMDB token is missing");
        }

        this.client = new OkHttpClient();
        this.token = token;
    }
    // get method version to be used for API calls for use case #1
    public String[][] get(String endpoint, String b) throws IOException {

        String url = BASE_URL + endpoint;

        Request request = new Request.Builder()
                .url(url)
                .get()
                .addHeader("accept", "application/json")
                .addHeader("Authorization", "Bearer " + token)
                .build();

        String jsonResponse = executeRequest(request);
//            JSONObject obj = response.body(); doesnt work
        JSONObject responseJSONobject = new JSONObject(jsonResponse);
        JSONArray results = responseJSONobject.getJSONArray("results");

        String[][] finalData = new String[results.length()][];
        int ind = 0;
        for(int i =0; i < results.length(); i++){
            if(results.getJSONObject(i).isNull("poster_path")) {
                finalData[i] = new String[]{
                        results.getJSONObject(i).getString("title"),
                        results.getJSONObject(i).getString("overview"),
                        results.getJSONObject(i).getString("release_date"),
                        String.valueOf(results.getJSONObject(i).getInt("id")),
                        ""};
            } else {
                finalData[i] = new String[]{
                        results.getJSONObject(i).getString("title"),
                        results.getJSONObject(i).getString("overview"),
                        results.getJSONObject(i).getString("release_date"),
                        String.valueOf(results.getJSONObject(i).getInt("id")),
                        results.getJSONObject(i).getString("poster_path")};
            }

        }
        System.out.println(Arrays.deepToString(finalData));
        return finalData;


    }

    // GET: Retrieve information from TMDB
    public String get(String endpoint) throws IOException {

        String url = BASE_URL + endpoint;

        Request request = new Request.Builder()
                .url(url)
                .get()
                .addHeader("accept", "application/json")
                .addHeader("Authorization", "Bearer " + token)
                .build();

        return executeRequest(request);
    }

    // POST: Send information to TMDB
    public String post(String endpoint, String jsonBody)
            throws IOException {

        String url = BASE_URL + endpoint;

        MediaType mediaType =
                MediaType.get("application/json; charset=utf-8");

        RequestBody body =
                RequestBody.create(jsonBody, mediaType);

        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .addHeader("accept", "application/json")
                .addHeader("Authorization", "Bearer " + token)
                .build();

        return executeRequest(request);
    }

    // Execute the HTTP request
    private String executeRequest(Request request)
            throws IOException {

        try (Response response =
                     client.newCall(request).execute()) {

            if (!response.isSuccessful()) {
                throw new IOException(
                        "API request failed: " + response.code());
            }

            if (response.body() == null) {
                throw new IOException("Empty response body");
            }

            return response.body().string();
        }
    }
}
