package data_access;

import use_case.initialise_predictions.BootstrapDataAccessInterface;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;

public class BootstrapDataGateway implements BootstrapDataAccessInterface {
    private static final String API_URL = "https://fantasy.premierleague.com/api/bootstrap-static/";
    private final OkHttpClient client;

    public BootstrapDataGateway() {
        this.client = new OkHttpClient();
    }

    public String getBootstrapData() throws IOException {
        final OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        Request request = new Request.Builder()
                .url(API_URL)
                .method("GET", null)
                .build();

        try {
            final Response response = client.newCall(request).execute();
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected code " + response);
            }
            return response.body().string();
        }
        catch (IOException e) {
            throw e;
        }
    }
}
