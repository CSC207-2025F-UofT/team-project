package data_access;

import okhttp3.HttpUrl;
import use_case.search.SearchDataAccessInterface;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;

/**
 * DAO for user data implemented using an API.
 */
public class ApiSearchDataAccessObject implements SearchDataAccessInterface {
    private final OkHttpClient client = new OkHttpClient();

    public static void main(String[] args) {
        ApiSearchDataAccessObject dao = new ApiSearchDataAccessObject();
        dao.search("Harry Potter");
    }

    /**
     * Construct this DAO for searching from the API.
     * @param query the query that pass into the API
     * @throws IOException if there is an IOException when accessing the file
     */
    public String search(String query) {
        HttpUrl url = HttpUrl.get("https://openlibrary.org/search.json")
                .newBuilder()
                .addQueryParameter("q", query)
                .build();

        Request request = new Request.Builder()
                .url(url)
                .build();

        String body = "";
        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful()) {
                body = response.body().string();
                System.out.println(body);
            } else {
                System.out.println("Request failed with code " + response.code());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return body;
    }
}
