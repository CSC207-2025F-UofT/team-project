package data_access;

import okhttp3.HttpUrl;
import use_case.search.SearchDataAccessInterface;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.util.List;

public class ApiSearchDataAccessObject implements SearchDataAccessInterface {
    private final OkHttpClient client = new OkHttpClient();

    public static void main(String[] args) {
        ApiSearchDataAccessObject dao = new ApiSearchDataAccessObject();
        dao.search("Harry Potter");
    }

    public List<String> search(String query) {
        HttpUrl url = HttpUrl.get("https://openlibrary.org/search.json")
                .newBuilder()
                .addQueryParameter("q", query)
                .build();

        Request request = new Request.Builder()
                .url(url)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful()) {
                String body = response.body().string();
                System.out.println(body);
            } else {
                System.out.println("Request failed with code " + response.code());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return List.of();
    }
}
