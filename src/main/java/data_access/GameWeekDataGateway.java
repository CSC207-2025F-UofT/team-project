package data_access;

import use_case.initialise_predictions.GameWeekDataAccessInterface;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;

public class GameWeekDataGateway implements GameWeekDataAccessInterface {
    private static final String GAMEWEEK_URL_TEMPLATE = "https://fantasy.premierleague.com/api/event/gw_id/live/";
    private final OkHttpClient client;

    public GameWeekDataGateway() {
        this.client = new OkHttpClient();
    }

    @Override
    public String getGameWeekLiveData(int gameWeekId) throws IOException {
        String url = GAMEWEEK_URL_TEMPLATE.replaceFirst("gw_id", String.valueOf(gameWeekId));

        Request request = new Request.Builder()
                .url(url)
                .method("GET", null)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected code " + response);
            }
            return response.body().string();
        }
    }
}
